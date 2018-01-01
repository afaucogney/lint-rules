package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2DefaultSchedulerDetectorTest {
  @Test fun schedulerSupportNone() {
    lint()
      .allowCompilationErrors()
      .files(stubSchedulerSupport, stubConsumer, stubScheduler, stubObservable, java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |
          |class Example {
          |  public void foo() {
          |    Observable.just(5)
          |  }
          |}""".trimMargin()))
      .issues(DEFAULT_SCHEDULER)
      .run()
      .expectClean()
  }

  @Test fun schedulerSupportComputation() {
    lint()
      .allowCompilationErrors()
      .files(stubSchedulerSupport, stubConsumer, stubScheduler, stubObservable, java("""
          |package foo;
          |
          |import java.util.concurrent.TimeUnit;
          |import io.reactivex.Observable;
          |
          |class Example {
          |  public void foo() {
          |    Observable.interval(5, TimeUnit.SECONDS)
          |  }
          |}""".trimMargin()))
      .issues(DEFAULT_SCHEDULER)
      .run()
      .expect("""
          |src/foo/Example.java:8: Warning: interval() is using its default scheduler [DefaultScheduler]
          |    Observable.interval(5, TimeUnit.SECONDS)
          |               ~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun schedulerSupportCustom() {
    lint()
      .allowCompilationErrors()
      .files(stubSchedulerSupport, stubConsumer, stubScheduler, stubSchedulers, stubObservable, java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |import io.reactivex.schedulers.Schedulers;
          |
          |class Example {
          |  public void foo() {
          |    Observable.interval(5, TimeUnit.SECONDS, Schedulers.computation())
          |  }
          |}""".trimMargin()))
      .issues(DEFAULT_SCHEDULER)
      .run()
      .expectClean()
  }
}