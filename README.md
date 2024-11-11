
![e1ff3706ea196f758818da129df6de53.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/251c47682956419c917bda1293249b01~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAgV2dsbHNz:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMzU2NjYxODM1MDgyNTczIn0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1727745943&x-orig-sign=bx79Ri6uQ7XwaNHmiSHMjLUgSwE%3D)
> 前言 ：众里寻它千百度, 蓦然回首，此种代码却在灯火阑珊处。 
>    
> [注解处理器在架构，框架中实战应用：MVVM中数据源提供Repository类的自动生成](https://juejin.cn/post/7392258195089162290)   

## 一、前言
**本文重点介绍思路：四种方式花式解决`Repository`中模版式的代码，逐级递增**  
**0.1 ：涉及到`Kotlin`、`协程`、`Flow、viewModel、Retrofit、Okhttp`相关用法**  
**0.2 ：涉及到`注解`、`反射`、`泛型`、`注解处理器`相关用法**  
**0.3 ：涉及到`动态代理`，`kotlin`中`suspend`方法反射调用及反射中异常处理**

本示例4个项目如图：

![380Xt8NSYZ.jpg](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/d1ff63643927401ab7f8188bda032a0b~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAgV2dsbHNz:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMzU2NjYxODM1MDgyNTczIn0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1727745943&x-orig-sign=XVJAD%2BPb64sDRkY50dz4VacKX%2Bc%3D)



1. 网络框架搭建的封装，到目前为止最为流行又很优雅的的是 **`Kotlin`+`协程`+`Flow`+`Retrofit`+`OkHttp`+`Repository`**
2. 先来看看中间各个类的职责：
![whiteboard_exported_image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/a99427e00ca143eab008557d30ca8e48~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAgV2dsbHNz:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMzU2NjYxODM1MDgyNTczIn0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1727745943&x-orig-sign=qFK82VpXfBSVvvPCRZ2uKRy2cgM%3D)

3. 从上图可以看出`单一职责：`  

**`NetApi：`** 负责网络接口配置,包括 请求地址，请求头，请求方式，参数等等所有配置    

**`Flow+Retrofit+Okhttp：`** 联合起来负责把**`NetApi`**中的各种配置组装成网络请求行为,并且通过**Flow** 组装成流，通过它可以控制该行为的异步方式，异步开始结束等等一系列的流行为。  

**`Repository：`** 负责**`Flow+Retrofit+Okhttp`** 请求结果的数据流,进行加工处理成我们想要的数据，大多数不需要处理的，可以直接给到 **`ViewModel`**  

**`ViewModel:`** 负责调用 **`Repository`**，拿到想要的数据然后提供给UI方展示使用或者相关使用

也可以看到 它的持有链 从右向左 一条线性持有：**`ViewModel`** 持有 **`Repository`**，**`Repository`**持有 **`Flow+Retrofit+Okhttp`** ,**`Flow+Retrofit+Okhttp`** 持有 **`NetApi`**

4. 最终我们可以得到：  
4.1. 网络请求行为 会根据 **`NetApi`** 写出模板式的代码，这块解决模版式的代码在 **`Retrofit`** 中它通过动态代理，把所有模版式的代码统一成了一个  
4.2. 同理：**`Repository`** 也是根据 **`NetApi`** 配置的接口，写成模版式的代码转换成流  

## 二、花式封装（一）
1.  **`NetApi`** 的配置：  

```
interface NetApi {

    // 示例get 请求
    @GET("https://www.wanandroid.com/article/list/0/json")
    suspend fun getHomeList(): CommonResult<WanAndroidHome>

    // 示例get 请求2
    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList(@Path("path") page: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList(@Path("path") page: Int, @Path("path") a: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList(@Path("path") page: Int, @Path("path") f: Float): CommonResult<WanAndroidHome>

    // 示例get 请求2
    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList2222(@Path("path") page: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList3333(@Path("path") page: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList5555(@Path("path") page: Int, @Query("d") ss: String, @HeaderMap map: Map<String, String>): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList6666(
        @Path("path") page: Int,
        @Query("d") float: Float,
        @Query("d") long: Long,
        @Query("d") double: Double,
        @Query("d") byte: Byte,
        @Query("d") short: Short,
        @Query("d") char: Char,
        @Query("d") boolean: Boolean,
        @Query("d") string: String,
        @Body body: RequestBodyWrapper
    ): CommonResult<WanAndroidHome>
    
    //示例post 请求
    @FormUrlEncoded
    @POST("https://www.wanandroid.com/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): String
    /************************* 以下只 示例写法，接口调不通，因为找不到那么多 公开接口  全是 Retrofit的用法 来测试 *****************************************************/


//    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")  //todo 固定 header
    @POST("https://xxxxxxx")
    suspend fun post1(@Body body: RequestBody): String

    //    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("https://xxxxxxx22222")
    suspend fun post12(@Body body: RequestBody, @HeaderMap map: Map<String, String>): String //todo  HeaderMap 多个请求头部自己填写

    suspend fun post1222(@Body body: RequestBody, @HeaderMap map: Map<String, Any>): String //todo  HeaderMap 多个请求头部自己填写
}
```
2. **`NetRepository`** 中是 根据 **`NetApi`** 写出下面类似的全模版式的代码：都是返回 **`Flow`** 流
```
class NetRepository private constructor() {
    val service by lazy { RetrofitUtils.instance.create(NetApi::class.java) }

    companion object {
        val instance by lazy { NetRepository() }
    }

    // 示例get 请求
    fun getHomeList() = flow { emit(service.getHomeList()) }

    // 示例get 请求2
    fun getHomeList(page: Int) = flow { emit(service.getHomeList(page)) }

    fun getHomeList(page: Int, a: Int) = flow { emit(service.getHomeList(page, a)) }

    fun getHomeList(page: Int, f: Float) = flow { emit(service.getHomeList(page, f)) }

    // 示例get 请求2
    fun getHomeList2222(page: Int) = flow { emit(service.getHomeList2222(page)) }

    fun getHomeList3333(page: Int) = flow { emit(service.getHomeList3333(page)) }

    fun getHomeList5555(page: Int, ss: String, map: Map<String, String>) = flow { emit(service.getHomeList5555(page, ss, map)) }

    fun getHomeList6666(
        page: Int, float: Float, long: Long, double: Double, byte: Byte,
        short: Short, char: Char, boolean: Boolean, string: String, body: RequestBodyWrapper
    ) = flow {
        emit(service.getHomeList6666(page, float, long, double, byte, short, char, boolean, string, body))
    }

    fun register(username: String, password: String, repassword: String) = flow { emit(service.register(username, password, repassword)) }

    //
//    /************************* 以下只 示例写法，接口调不通，因为找不到那么多 公开接口  全是 Retrofit的用法 来测试 *****************************************************/
//
//
    fun post1(body: RequestBody) = flow { emit(service.post1(body)) }

    fun post12(body: RequestBody, map: Map<String, String>) = flow { emit(service.post12(body, map)) }

    fun post1222(id: Long, asr: String) = flow {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["asr"] = asr
        val mapHeader = HashMap<String, Any>()
        mapHeader["v"] = 1000
        mapHeader["device_sn"] = "Avidfasfa1213"
        emit(service.post1222(RequestBodyWrapper(Gson().toJson(map)), mapHeader))
    }
}
```

3. **`viewModel`** 调用端：
```
class MainViewModel : BaseViewModel() {

    private val repository by lazy { NetRepository.instance }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            repository.getHomeList(page).onEach {
                android.util.Log.e("MainViewModel", "one 111 ${it.data?.datas!![0].title}")
            }
        }
    }
}
```
————————————————————————————————————————

4. **`上面花式玩法(一)：` 此种写法被广泛称作 `最优雅的一套网络封装` 框架，**  

**`绝大多数中、大厂`** 基本也就封装到此为止了 

**哪还能有什么玩法？**  

可能会有人想到 借助 **`Hilt`** ，**`Dagger2`** ，**`Koin`** 来创建 **`Retrofit`**,和创建 **`repository`**,创建 **`ViewModel`**
这里不是讨论依赖注入创建对象的事情 

**哪还有什么玩法？** 

**有，必须有的。**

## 三、花式封装（二）

1. 既然上面是 **`Repository`** 类中，所有写法都是固定模版式的代码，那么让其根据 **`NetApi：`** 自动生成 **`Repository`** 类，我们这里借用注解处理器。
2. 具体怎么使用介绍，请参考：  
 [注解处理器在架构，框架中实战应用：MVVM中数据源提供Repository类的自动生成](https://juejin.cn/post/7392258195089162290)    
3. 本项目中只需要编译   **`app_wx2`** 工程  
4. 在下图中找到

![img_v3_02f0_d5bd4278-53ac-4008-aac2-abcfdf81668g.jpg](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/22899f2578cb4c489c88df288d3b0e64~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAgV2dsbHNz:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMzU2NjYxODM1MDgyNTczIn0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1727745943&x-orig-sign=aGbENo%2Bl0LmrdLeT6LJuwre2KRM%3D)
5. viewModel调用端
```
class MainViewModel : BaseViewModel() {

    private val repository by lazy { RNetApiRepository() }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            val time = System.currentTimeMillis()
            repository.getHomeList(page).onEach {
                android.util.Log.e("MainViewModel", "two 222 ${it.data?.datas!![0].title}")
                android.util.Log.e("MainViewModel", "耗时:${(System.currentTimeMillis() - time)} ms")
            }
        }
    }
}
```

6. 如果 **`Repository`** 中某个接口方法需要特殊处理怎么办？比如下图，请求前处理一下，从 拿到数据后我需要再次转化处理之后再给到 **`viewModel`** 怎么办？

```
//我这个接口 ，请求前需要 判断处理一下，拿到数据后也需要再处理一下
fun post333(id: Long, asr: String, m: String, n: String, list: List<String>) = flow {
    val map = mutableMapOf<String, Any>()
    map["id"] = id
    map["asr"] = asr
    val mapHeader = HashMap<String, Any>()
    mapHeader["v"] = 1000
    mapHeader["device_sn"] = "Avidfasfa1213"

    //接口调用前 根据 需要处理操作
    list.forEach {
        if (map.containsKey(id.toString())) {
            ///
        }
    }

    val result = service.post1222(RequestBodyWrapper(Gson().toJson(map)), mapHeader)
    // 拿到数据后需要处理操作
    val result1 = result
    emit(result1)
}.map {
    //需要再转化一下
    it
}.filter {
    //过滤一下
    it.length == 3
}
```
7. 可以在 接口 **`NetApi`** 中该方法上配置 **`@Filter`** 注解过滤 ，该方法需要自己特殊处理，不自动生成，如下
```

@Filter
@POST("https://xxxxxxx22222")
suspend fun post333(@Body body: RequestBody, @HeaderMap map: Map<String, Any>): String
```
8. 如果想 post请求的 **`RequestBody`** 内部参数单独出来进入方法传参，可以加上 在 **`NetApi`** 中方法加上 **`@PostBody`**：如下：

```
@PostBody("{"ID":"Long","name":"String"}")
@POST("https://www.wanandroid.com/user/register")
suspend fun testPostBody222(@Body body: RequestBody): String
```

这样 **该方法生成出来的对应方法就是：**

```
public suspend fun testPostBody222(ID: Long, name: java.lang.String): Flow<String> =
    kotlinx.coroutines.flow.flow {
        val map = mutableMapOf<String, Any>()
        map["ID"] = ID
        map["name"] = name
        val result = service.testPostBody222(com.wx.test.api.retrofit.RequestBodyCreate.toBody(com.google.gson.Gson().toJson(map)))
        emit(result)
    }
```
怎么特殊处理，单独手动建一个Repository,针对该方法，单独写，特殊就要特殊手动处理，但是大多数模版式的代码，都可以让其自动生成。


————————————————————————————————————————

到了这里，我们再想， **`NetApi`** 是一个接口类，  
但是实际上没有写接口实现类啊， 它怎么实现的呢？   
我们上面 **`花式玩法（二）`** 中虽然是自动生成的，但是还是有方法体，

**可不可以再省略点？** 

**可以，必须有！**

## 四、花式玩法（三） 

1. 我们可以根据 **`NetApi`** 里面的配置，自动生成 **`INetApiRepository`** 接口类，
接口名和参数 都和 **`NetApi`** 保持一致，唯一区别就是返回的对象变成了 **`Flow<T>`** 了，  
  这样在 **`Repository`** 中就把数据转变为 **`flow`**  流了
2. 配置让代码自动生成的类：
```
@AutoCreateRepositoryInterface(interfaceApi = "com.wx.test.api.net.NetApi")
class KaptInterface {
}
```
  生成的接口类 **`INetApiRepository`** 代码如下：
  ```

public interface INetApiRepository {
    public fun getHomeList(): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList(page: Int): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList(page: Int, f: Float): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList(page: Int, a: Int): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList2222(page: Int): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList3333(page: Int): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList5555(
        page: Int,
        ss: String,
        map: Map<String, String>
    ): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeList6666(
        page: Int,
        float: Float,
        long: Long,
        double: Double,
        byte: Byte,
        short: Short,
        char: Char,
        boolean: Boolean,
        string: String,
        body: RequestBodyWrapper
    ): Flow<CommonResult<WanAndroidHome>>

    public fun getHomeListA(page: Int): Flow<Call<HomeData>>

    public fun getHomeListB(page: Int): Flow<HomeData>

    public fun post1(body: RequestBody): Flow<String>

    public fun post12(body: RequestBody, map: Map<String, String>): Flow<String>

    public fun post1222(body: RequestBody, map: Map<String, Any>): Flow<String>

    public fun register(
        username: String,
        password: String,
        repassword: String
    ): Flow<String>

    public fun testPostBody222(ID: Long, name: java.lang.String): Flow<String>
}
```

3. **`Repository`** 职责承担的调用端：用动态代理：
```

class RepositoryPoxy private constructor() : BaseRepositoryProxy() {

    val service = NetApi::class.java
    val api by lazy { RetrofitUtils.instance.create(service) }


    companion object {
        val instance by lazy { RepositoryPoxy() }
    }

    fun <R> callApiMethod(serviceR: Class<R>): R {
        return Proxy.newProxyInstance(serviceR.classLoader, arrayOf(serviceR)) { proxy, method, args ->
            flow {
                val funcds = findSuspendMethod(service, method.name, args)
                if (args == null) {
                    emit(funcds?.callSuspend(api))
                } else {
                    emit(funcds?.callSuspend(api, *args))
                }
//                emit((service.getMethod(method.name, *parameterTypes)?.invoke(api, *(args ?: emptyArray())) as Call<HomeData>).execute().body())
            }.catch {
                if (it is InvocationTargetException) {
                    throw Throwable(it.targetException)
                } else {
                    it.printStackTrace()
                    throw it
                }
            }
        } as R
    }
}
```
4. **`BaseRepositoryProxy`** 中内容：
```

open class BaseRepositoryProxy {

    private val map by lazy { mutableMapOf<String, KFunction<*>?>() }
    private val sb by lazy { StringBuffer() }

    @OptIn(ExperimentalStdlibApi::class)
    fun <T : Any> findSuspendMethod(service: Class<T>, methodName: String, args: Array<out Any>): KFunction<*>? {
        sb.delete(0, sb.length)
        sb.append(service.name)
            .append(methodName)
        args.forEach {
            sb.append(it.javaClass.typeName)
        }
        val key = sb.toString()
        if (!map.containsKey(key)) {
            val function = service.kotlin.memberFunctions.find { f ->
                var isRight = 0
                if (f.name == methodName && f.isSuspend) {
                    if (args.size == 0 && f.parameters.size == 1) {
                        isRight = 2
                    } else {
                        f.parameters.forEachIndexed { index, it ->
                            if (index > 0 && args.size > 0) {
                                if (args.size == 0) {
                                    isRight = 2
                                    return@forEachIndexed
                                }
                                if (it.type.javaType.typeName == javaClassTransform(args[index - 1].javaClass).typeName) {
                                    isRight = 2
                                } else {
                                    isRight = 1
                                    return@forEachIndexed
                                }
                            }
                        }
                    }
                }
                //方法名一直  是挂起函数  方法参数个数一致， 参数类型一致
                f.name == methodName && f.isSuspend && f.parameters.size - 1 == args.size && isRight == 2
            }
            map[key] = function
        }
        return map[key]
    }

    private fun javaClassTransform(clazz: Class<Any>) = when (clazz.typeName) {
        "java.lang.Integer" -> Int::class.java
        "java.lang.String" -> String::class.java
        "java.lang.Float" -> Float::class.java
        "java.lang.Long" -> Long::class.java
        "java.lang.Boolean" -> Boolean::class.java
        "java.lang.Double" -> Double::class.java
        "java.lang.Byte" -> Byte::class.java
        "java.lang.Short" -> Short::class.java
        "java.lang.Character" -> Char::class.java
        "SingletonMap" -> Map::class.java
        "LinkedHashMap" -> MutableMap::class.java
        "HashMap" -> HashMap::class.java
        "Part" -> MultipartBody.Part::class.java
        "RequestBody" -> RequestBody::class.java
        else -> {
            if ("RequestBody" == clazz.superclass.simpleName) {
                RequestBody::class.java
            } else {
                Any::class.java
            }
        }
    }
}
```
5. ViewModel中调用端：
```
class MainViewModel : BaseViewModel() {

    private val repository by lazy { RepositoryPoxy.instance }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            val time = System.currentTimeMillis()
            repository.callApiMethod(INetApiRepository::class.java).getHomeList(page).onEach {
                android.util.Log.e("MainViewModel", "three 333 ${it.data?.datas!![0].title}")
                android.util.Log.e("MainViewModel", "耗时:${(System.currentTimeMillis() - time)} ms")
            }
        }
    }
}
```

————————————————————————————————————  

6. 上面生成的接口类 **`INetApiRepository`** 其实方法和 **`NetApi`** 拥有相似的模版，唯一区别就是返回类型，一个是对象，一个是Flow<T> 流的对象
    
    **还能省略吗？**

    **有，必须有**
    
## 五、花式玩法（四）
1.  直接修改  **`RepositoryPoxy`** ，作为Reposttory的职责 ，如下：
    
    
   ```
class RepositoryPoxy private constructor() : BaseRepositoryProxy() {

    val service = NetApi::class.java
    val api by lazy { RetrofitUtils.instance.create(service) }


    companion object {
        val instance by lazy { RepositoryPoxy() }
    }

    fun <R> callApiMethod(clazzR: Class<R>, methodName: String, vararg args: Any): Flow<R> {
        return flow {
            val clssss = mutableListOf<Class<out Any>>()
            args?.forEach {
                clssss.add(javaClassTransform(it.javaClass))
            }
            val parameterTypes = clssss.toTypedArray()
            val call = (service.getMethod(methodName, *parameterTypes)?.invoke(api, *(args ?: emptyArray())) as Call<R>)
            call?.execute()?.body()?.let {
                emit(it as R)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun <R> callApiSuspendMethod(clazzR: Class<R>, methodName: String, vararg args: Any): Flow<R> {
        return flow {
            val funcds = findSuspendMethod(service, methodName, args)
            if (args == null) {
                emit(funcds?.callSuspend(api) as R)
            } else {
                emit(funcds?.callSuspend(api, *args) as R)
            }
        }
    }
}
```
    
2. ViewModel中调用入下：
```
class MainViewModel : BaseViewModel() {

    private val repository by lazy { RepositoryPoxy.instance }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            val time = System.currentTimeMillis()
            repository.callApiSuspendMethod(HomeData::class.java, "getHomeListB", page).onEach {
                android.util.Log.e("MainViewModel", "four 444 ${it.data?.datas!![0].title}")
                android.util.Log.e("MainViewModel", "耗时:${(System.currentTimeMillis() - time)} ms")
            }
        }
    }
}
```  
## 六、总结
    
通过上面4中花式玩法:
1.  **花式玩法1**： 我们知道了最常见最优雅的写法，但是模版式 **`repository`** 代码太多，而且需要手动写
2.  **花式玩法2**： 把花式玩法1中的模版式 **`repository`** ，让其自动生成,对于特殊的方法，单独手动再写个 **`repository`** ，这样让大多数模版式代码全自动生成
3.  **花式玩法3**： **`NetApi`**,可以根据配置，动态代理生成网络请求行为，该行为统一为动态代理实现，无需对接口类 **`NetApi`** 单独实现，那么我们的 **`repository`** 也可以 生成一个接口类 **`INetApiRepository`** ，然后动态代理实现其内部 方法体逻辑
4.  **花式玩法4**：我连花式玩法3中的接口类 **`INetApiRepository`** 都不需要了，直接反射搞定所有。
5. 同时可以学习到，**注解、反射、泛型、注解处理器、动态代理**

## 七、我的其他开源
#### [那些大厂架构师是怎样封装网络请求的？](https://juejin.cn/post/7435904232597372940)
#### [Kotlin+协程+Flow+Retrofit+OkHttp这么好用，不运行安装到手机可以调试接口吗?可以自己搭建一套网络请求工具](https://juejin.cn/post/7406675078810910761)
#### [花式封装：Kotlin+协程+Flow+Retrofit+OkHttp +Repository，倾囊相授,彻底减少模版代码进阶之路](https://juejin.cn/post/7417847546323042345)
#### [注解处理器在架构，框架中实战应用：MVVM中数据源提供Repository类的自动生成](https://juejin.cn/post/7392258195089162290)

## 八、我的全动态插件化框架WXDynamicPlugin介绍文章：
#### [(一) 插件化框架开发背景：零反射，零HooK,全动态化，插件化框架，全网唯一结合启动优化的插件化架构](https://juejin.cn/post/7347994218235363382)
#### [(二）插件化框架主要介绍：零反射，零HooK,全动态化，插件化框架，全网唯一结合启动优化的插件化架构](https://juejin.cn/post/7367676494976532490)
#### [(三）插件化框架内部详细介绍: 零反射，零HooK,全动态化，插件化框架，全网唯一结合启动优化的插件化架构](https://juejin.cn/post/7368397264026370083)
#### [(四）插件化框架接入详细指南：零反射，零HooK,全动态化，插件化框架，全网唯一结合启动优化的插件化架构](https://juejin.cn/post/7372393698230550565)
#### [(五) 大型项目架构：全动态插件化+模块化+Kotlin+协程+Flow+Retrofit+JetPack+MVVM+极限瘦身+极限启动优化+架构示例+全网唯一](https://juejin.cn/post/7381787510071934985)
#### [(六) 大型项目架构：解析全动态插件化框架WXDynamicPlugin是如何做到全动态化的？](https://juejin.cn/post/7388891131037777929)
#### [(七) 还在不断升级发版吗？从0到1带你看懂WXDynamicPlugin全动态插件化框架？](https://juejin.cn/post/7412124636239904819)
#### [(八) Compose插件化：一个Demo带你入门Compose，同时带你入门插件化开发](https://juejin.cn/post/7425434773026537483)
#### [(九) 花式高阶：插件化之Dex文件的高阶用法，极少人知道的秘密 ](https://juejin.cn/spost/7428216743166771212)
#### 感谢阅读，欢迎给给个星，你们的支持是我开源的动力
## 欢迎光临：

#### * **[我的掘金地址](https://juejin.cn/user/356661835082573)**   
    
    
    

    
    











