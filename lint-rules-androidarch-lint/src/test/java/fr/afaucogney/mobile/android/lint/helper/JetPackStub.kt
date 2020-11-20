package fr.niji.mobile.android.socle.lint.helper

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin

val mutableLiveDataStub = kotlin("""
        package androidx.lifecycle
        class MutableLiveData<T>
    """).indented()

val liveDataStub = kotlin("""
        package androidx.lifecycle
        class LiveData
    """).indented()