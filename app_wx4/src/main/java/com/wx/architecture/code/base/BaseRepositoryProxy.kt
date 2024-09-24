package com.wx.architecture.code.base

import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.javaType

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

    fun javaClassTransform(clazz: Class<Any>) = when (clazz.typeName) {
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