package fr.niji.mobile.android.socle.lint.rules.contract

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import fr.niji.mobile.android.socle.lint.helper.liveDataStub
import fr.niji.mobile.android.socle.lint.helper.mutableLiveDataStub
import fr.niji.mobile.android.socle.lint.rules.contract.UseMutableLiveDataInViewModelContractDetector.Companion.ISSUE_MUTABLELIVEDATA_IN_FEATURE_CONTRACT
import org.junit.Test

class UseMutableLiveDataInViewModelContractDetectorTest {

    @Test
    fun testSingleError() {
        lint()
            .allowMissingSdk()
            .files(mutableLiveDataStub, kotlin("""
                |package foo
                |
                |import androidx.lifecycle.MutableLiveData
                |
                |interface IMyFeatureContract {
                |   interface ViewModel {
                |       fun observeRememberMeState(): MutableLiveData<Any>
                |   }
                |}""".trimMargin()))
            .issues(ISSUE_MUTABLELIVEDATA_IN_FEATURE_CONTRACT)
            .run()
            .expectErrorCount(1)
    }

    @Test
    fun testSingleVarError() {
        lint()
            .allowMissingSdk()
            .files(mutableLiveDataStub, kotlin("""
                |package foo
                |
                |import androidx.lifecycle.MutableLiveData
                |
                |interface IMyFeatureContract {
                |   interface ViewModel {
                |       fun observeRememberMeState(): MutableLiveData<Any>
                |       var tutu : MutableLiveData<Any>
                |   }
                |}""".trimMargin()))
            .issues(ISSUE_MUTABLELIVEDATA_IN_FEATURE_CONTRACT)
            .run()
            .expectErrorCount(2)
    }

    @Test
    fun testDualError() {
        lint()
            .allowMissingSdk()
            .files(mutableLiveDataStub, kotlin("""
                |package foo
                |
                |import androidx.lifecycle.MutableLiveData
                |
                |interface IMyFeatureContract {
                |   interface ViewModel {
                |       fun observeRememberMeState1(): MutableLiveData<Any>
                |       fun observeRememberMeState2(): MutableLiveData<Any>
                |   }
                |}""".trimMargin()))
            .issues(ISSUE_MUTABLELIVEDATA_IN_FEATURE_CONTRACT)
            .run()
            .expectErrorCount(2)
    }


    @Test
    fun testHybrid() {
        lint()
            .allowMissingSdk()
            .files(liveDataStub, mutableLiveDataStub, kotlin("""
                |package foo
                |
                |import androidx.lifecycle.LiveData
                |import androidx.lifecycle.MutableLiveData
                |
                |interface IMyFeatureContract {
                |   interface ViewModel {
                |       fun observeRememberMeState1(): LiveData<Any>
                |       fun observeRememberMeState2(): MutableLiveData<Any>
                |   }
                |}""".trimMargin()))
            .issues(ISSUE_MUTABLELIVEDATA_IN_FEATURE_CONTRACT)
            .run()
            .expectErrorCount(1)
    }

    @Test
    fun testSuccess() {
        lint()
            .allowMissingSdk()
            .files(liveDataStub, kotlin("""
                |package foo;
                |
                |import androidx.lifecycle.LiveData
                |
                |interface IMyFeatureContract {
                |   interface ViewModel {
                |       fun observeRememberMeState1(): LiveData<Any>
                |       fun observeRememberMeState2(): LiveData<Any>
                |   }
                |}""".trimMargin()))
            .issues(ISSUE_MUTABLELIVEDATA_IN_FEATURE_CONTRACT)
            .run()
            .expectClean()
    }
}