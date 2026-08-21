#include <vulkan/vulkan.h>
#include <dlfcn.h>
#include <cstring>
#include <jni.h>

#define QUALCOMM_VENDOR_ID 0x5143

bool checkIsAdreno(VkPhysicalDevice physicalDevice, PFN_vkGetPhysicalDeviceProperties pfnGetProps) {
    VkPhysicalDeviceProperties properties;
    pfnGetProps(physicalDevice, &properties);

    if (properties.vendorID == QUALCOMM_VENDOR_ID) {
        return true;
    }
    if (strstr(properties.deviceName, "Adreno") != nullptr ||
        strstr(properties.deviceName, "ADRENO") != nullptr) {
        return true;
    }
    return false;
}

extern "C" {
    JNIEXPORT jboolean JNICALL Java_com_vectras_vm_utils_GpuHelper_isAdreno(JNIEnv* env, jobject obj) {
        // Try libvulkan.so at runtime.
        void* vulkanLib = dlopen("libvulkan.so", RTLD_NOW | RTLD_LOCAL);
        if (!vulkanLib) {
            // No drivers or outdated Android.
            return false;
        }

        // Get at vkGetInstanceProcAddr.
        auto pfnGetInstanceProcAddr = reinterpret_cast<PFN_vkGetInstanceProcAddr>(
                dlsym(vulkanLib, "vkGetInstanceProcAddr")
        );

        if (!pfnGetInstanceProcAddr) {
            dlclose(vulkanLib);
            return false;
        }

        // Get at vkCreateInstance.
        auto pfnCreateInstance = reinterpret_cast<PFN_vkCreateInstance>(
                pfnGetInstanceProcAddr(nullptr, "vkCreateInstance")
        );

        if (!pfnCreateInstance) {
            dlclose(vulkanLib);
        return false;
        }

        VkApplicationInfo appInfo = {};
        appInfo.sType = VK_STRUCTURE_TYPE_APPLICATION_INFO;
        appInfo.apiVersion = VK_API_VERSION_1_0;

        VkInstanceCreateInfo createInfo = {};
        createInfo.sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
        createInfo.pApplicationInfo = &appInfo;

        VkInstance instance = VK_NULL_HANDLE;
        if (pfnCreateInstance(&createInfo, nullptr, &instance) != VK_SUCCESS || !instance) {
            dlclose(vulkanLib);
            return false;
        }

        // Get at Instance
        auto pfnEnumeratePhysicalDevices = reinterpret_cast<PFN_vkEnumeratePhysicalDevices>(
                pfnGetInstanceProcAddr(instance, "vkEnumeratePhysicalDevices")
        );
        auto pfnGetPhysicalDeviceProperties = reinterpret_cast<PFN_vkGetPhysicalDeviceProperties>(
                pfnGetInstanceProcAddr(instance, "vkGetPhysicalDeviceProperties")
        );
        auto pfnDestroyInstance = reinterpret_cast<PFN_vkDestroyInstance>(
                pfnGetInstanceProcAddr(instance, "vkDestroyInstance")
        );

        bool hasAdreno = false;

        if (pfnEnumeratePhysicalDevices && pfnGetPhysicalDeviceProperties) {
            uint32_t deviceCount = 0;
            pfnEnumeratePhysicalDevices(instance, &deviceCount, nullptr);

            if (deviceCount > 0) {
                VkPhysicalDevice devices[deviceCount];
                pfnEnumeratePhysicalDevices(instance, &deviceCount, devices);

                for (uint32_t i = 0; i < deviceCount; ++i) {
                    if (checkIsAdreno(devices[i], pfnGetPhysicalDeviceProperties)) {
                        hasAdreno = true;
                        break;
                    }
                }
            }
        }

        // Cleanup.
        if (pfnDestroyInstance) {
            pfnDestroyInstance(instance, nullptr);
        }
        dlclose(vulkanLib);

        return hasAdreno;
    }
}