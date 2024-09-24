//package com.annotation_compiler;
//
//import com.atar.annotations.CreateService;
//import com.google.auto.service.AutoService;
//
//import java.io.Writer;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Filer;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
// java 写法
//@AutoService(Processor.class)
//public class AnnotationsCompiler extends AbstractProcessor {
//
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
//    }
//
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> sets = new HashSet<String>();
//        sets.add(CreateService.class.getCanonicalName());
//        return sets;
//    }
//
//    private Filer filer;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        filer = processingEnv.getFiler();
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
//        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(CreateService.class);
//        Writer writer = null;
//        for (Element element : elementsAnnotatedWith) {
//            //得到包名
//            String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
//            String className = element.getSimpleName().toString();
//            try{
////                Class<?>[] classes = element.getClass().getDeclaredClasses();
////                for(Class<?> c:classes){
////                    log("---333"+c.getSimpleName());
////                }
////                log("-->"+element.getClass().getClasses().toString());
//
////                log("-66->"+ processingEnv.getTypeUtils().toString());
////                for(){
////
////                }
////               Class clazz = Class.forName(className);
////              Class<? extends Element> cls =       element.getEnclosedElements();
////              for(Class c:cls){
////                  log("c888---"+element.getEnclosedElements().toString()); //获取方法
////              }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
////            log("99--->"+element.getModifiers().toString());
//
////            log("element--88->" + element.getSimpleName() + "---pakageName-->" + packageName);
////            try {
////                JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + className + "");
////                writer = sourceFile.openWriter();
////                writer.write("package " + packageName + ";\n");
////                writer.write("import " + packageName + ".base.activity.BaseRepository;\n");
////                writer.write("import " + packageName + ".data.BaiduDataBean;\n");
////                writer.write("import " + packageName + ".data.BaseResponse;\n");
////                writer.write("import " + packageName + ".net.impl." + className + ";\n");
////                writer.write("import javax.inject.Inject;\n");
//////                import com.example.myapplication.net.impl.RestService
//////import javax.inject.Inject
////                writer.write("class " + className + "Impl  constructor() : BaseRepository<" + className + ">()," +
////                        className + "{}\n");
////            } catch (Exception e) {
////                e.printStackTrace();
////            } finally {
////                if (writer != null) {
////                    try {
////                        writer.close();
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
//        }
//        return false;
//    }
//
//    private void log(String message) {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
//    }
//}