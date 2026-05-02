#include <jni.h>
#include <unistd.h>
#include <thread>

#if defined(__aarch64__)
    #include <sys/auxv.h>
    #include <asm/hwcap.h>
#elif defined(__x86_64__)
    #include <cpuid.h>
#endif

extern "C" {
    JNIEXPORT jint JNICALL
    Java_com_vectras_vm_utils_CpuHelper_getCpuCores(JNIEnv* env, jobject obj) {
        return sysconf(_SC_NPROCESSORS_CONF);
    }

    JNIEXPORT jint JNICALL
    Java_com_vectras_vm_utils_CpuHelper_getActiveCpuCores(JNIEnv* env, jobject obj) {
        return sysconf(_SC_NPROCESSORS_ONLN);
    }

    JNIEXPORT jint JNICALL
    Java_com_vectras_vm_utils_CpuHelper_getCpuThreads(JNIEnv* env, jobject obj) {
        unsigned int threads = std::thread::hardware_concurrency();
        return (threads > 0) ? (jint)threads : 1;
    }

    JNIEXPORT jint JNICALL
    Java_com_vectras_vm_utils_CpuHelper_check64Bit(JNIEnv* env, jobject obj) {
        // 1: 64-bit, 0: 32-bit, -1: Unknown or error.

        #if defined(__aarch64__)
            unsigned long hwcaps = getauxval(AT_HWCAP);
            return (hwcaps > 0) ? 1 : 0;
        #elif defined(__x86_64__)
            unsigned int eax, ebx, ecx, edx;
            if (__get_cpuid(0x80000001, &eax, &ebx, &ecx, &edx)) {
                return (edx & (1 << 29)) ? 1 : 0;
            }
            return -1; // Failed to call CPUID.
        #endif

        return 0;
    }
}