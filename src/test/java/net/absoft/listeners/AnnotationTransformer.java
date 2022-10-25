package net.absoft.listeners;

import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;


public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setDescription("Undated descriprion: " + testMethod.getName());
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
