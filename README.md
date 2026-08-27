<div align="center">
<p align="center">
  <img src="resources/vectrasvm2026.png" style="width: 30%;" />
</p>
</div>

# 📢 Announcement

We are no longer collaborating with the official Vectras VM as of July 30, 2026. Therefore, this fork will henceforth be separate and have a new direction. The Vectras VM version in this fork will still be maintained and updated as before. Thank you for using both the official Vectras VM and this fork version over the past time. We also thank those who created, maintain, and contribute to Vectras VM. You can visit the [official version here](https://github.com/xoureldeen/Vectras-VM-Android).

> [!CAUTION]
> For security reasons, you should USE OR UPDATE TO VERSION 4.6.3 OR LATER. You need to uninstall the previous version before updating, your virtual machines will not be deleted. If for some reason you wish to use a version older than 4.6.3, ENSURE YOU KNOW WHAT YOU ARE DOING AND YOU SHOULD USE THE VERSION RELEASED HERE, IN THE OFFICIAL REPOSITORY, OR FROM ANOTHER TRUSTED SOURCE, AVOID USING TERMUX:X11 OR RUN ANY UNFAMILIAR COMMANDS IN THE TERMINAL. IF YOU STILL INSIST ON USING TERMUX:X11, ENSURE THAT NOTHING INTERFERES WITH VECTRAS VM UNDER ANY CIRCUMSTANCES. If you are a developer, `vectrasvm.jks` has been renamed to `testkey.jks`, YOU SHOULD USE THIS OLD KEY ONLY FOR SAFE TESTING PURPOSES AND NOT FOR PRODUCTION RELEASES.

# 🪴 Vectras VM
![GitHub Repo stars](https://img.shields.io/github/stars/AnBui2004/Vectras-VM-Emu-Android)
![GitHub watchers](https://img.shields.io/github/watchers/AnBui2004/Vectras-VM-Emu-Android)
![GitHub forks](https://img.shields.io/github/forks/AnBui2004/Vectras-VM-Emu-Android)
[![Total downloads](https://img.shields.io/github/downloads/AnBui2004/Vectras-VM-Emu-Android/total)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases)
[![Software License][ico-license]](LICENSE)

Welcome to Vectras VM! A virtual machine app for Android based on QEMU that lets you emulate various OSes including: [![Windows](https://custom-icon-badges.demolab.com/badge/Windows-0078D6?logo=windows11&logoColor=white)](https://www.microsoft.com/en-us/windows) [![Linux](https://img.shields.io/badge/Linux-FCC624?logo=linux&logoColor=black)](https://www.linux.org/) [![macOS](https://img.shields.io/badge/macOS-000000?logo=apple&logoColor=F0F0F0)](https://www.apple.com/macos) [![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)](https://www.android.com/).

If you need help, you can join [our community](https://anbui.ovh/community/). And here's a suggestion if you're new to Vectras VM:

[![Tutorial for beginners](https://img.youtube.com/vi/L6ADe5aVp0Y/mqdefault.jpg)](https://www.youtube.com/watch?v=L6ADe5aVp0Y)

## 📱 Device Compatibility

Works fine on devices manufactured in 2021 or later and devices equipped with Snapdragon 855 CPU or better. Unexpected problems can occur on devices from Chinese or lesser-known manufacturers because they often have their own operating system optimization methods that differ from stock Android.

### ⚡ Minimum System Requirements
- Android 6.0 and up.
- 3GB RAM (1GB of free RAM).
- A good processor.

### 💡 Recommended System Requirements
- Android 8.1 and up.
- 8GB RAM (3GB of free RAM).
- CPU and Android OS support 64-bit.
- Snapdragon 855 CPU or better.
- Integrated or removable cooling system (if running operating systems from 2010 to present).
> [!TIP]
> If the OS you are trying to emulate crashes, try using an older version.

# ⬇️ Installation

You can download Vectras VM from the [releases](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases) page.

### ⚙️ Bootstraps
QEMU 7.2.22 - 3dfx is here!

- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-3dfx-july-2026-vectras-vm-arm64-v8a.tar.gz)
- [For Android ARM (32-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-july-2026-vectras-vm-armeabi-v7a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-3dfx-july-2026-vectras-vm-x86_64.tar.gz)
- [For Android x86 (32-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-july-2026-vectras-vm-x86.tar.gz)

### 💽 3Dfx Wrappers
Play classic games with 3Dfx support on Windows 95, 98, ME, 2000, and XP! Turnip & Zink will help improve performance if your device has a supported Adreno GPU. Note: It may be unstable and have low performance.

[⬇️ Download here](https://github.com/AnBui2004/Vectras-VM-Emu-Android/blob/master/3dfx/4.1.1%2B/3dfx-wrappers-i686.iso)

### 😼 Advanced
Advanced options are [here](ADVANCED.md).

# ❤️ Thanks to
- [Alpine Linux](https://www.alpinelinux.org/)
- [Glide](https://github.com/bumptech/glide)
- [Gson](https://github.com/google/gson)
- [Mesa for Android Container](https://github.com/lfdevs/mesa-for-android-container)
- [OkHttp](https://github.com/square/okhttp)
- [PRoot](https://github.com/termux/proot)
- [QEMU](https://github.com/qemu/qemu)
- [QEMU MESA GL/3Dfx Glide Pass-Through](https://github.com/kjliew/qemu-3dfx)
- [Termux](https://github.com/termux)
- [Vectras VM (Official)](https://github.com/xoureldeen/Vectras-VM-Android)
- [ZoomImageView](https://github.com/k1slay/ZoomImageView)

# 📜 License

This project is licensed under the following terms:

* **Directory `app/src/main/java/com/anbui/elephant` (excluding `vm`)**: Licensed under [Apache License 2.0](LICENSE-APACHE-2.0).
* **The rest of the project**: Licensed under [GNU General Public License v2.0](LICENSE).
* **Brand Assets (Logo / Icon / Name):** The logo, icon, and brand name are the intellectual property of **Nguyen Bao An Bui**. All rights reserved. You are **not permitted** to use this logo, icon, or brand name for forks, commercial products, or republished applications without written consent. This applies to the following files: `nbablab.png`, `notebook_param_48.png`, and `nguyen_bao_an_bui.xml`.

[ico-version]: https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white
[ico-license]: https://img.shields.io/badge/License-GPL_v2-blue.svg

[link-repo]: https://github.com/AnBui2004/Vectras-VM-Emu-Android/
[link-releases]: https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/
