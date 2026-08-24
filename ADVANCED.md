# 😼 Advanced
⬇️ Advanced options waiting for you to discover are below. ⬇️

Do you need help? Join [our community](https://anbui.ovh/community/) now!

## 🔃 Upgrade tool
Upgrade or change the Qemu version you want to use without resetting the Vectras VM using the Terminal.

QEMU 11.0.0 (requires Vectras VM 4.0.8+):
```bash
apk add bash && curl -H 'Cache-Control: no-cache' -o setup.sh https://raw.githubusercontent.com/AnBui2004/Vectras-VM-Emu-Android/refs/heads/master/qemu/11.0.0/upgrade.sh && chmod +rwx setup.sh && ./setup.sh; rm setup.sh
```

QEMU 9.2.4 - 3dfx (requires Vectras VM 3.5.0+):
```bash
apk add bash && curl -H 'Cache-Control: no-cache' -o setup.sh https://raw.githubusercontent.com/AnBui2004/Vectras-VM-Emu-Android/refs/heads/master/qemu/9.2.4/upgrade.sh && chmod +rwx setup.sh && ./setup.sh; rm setup.sh
```

QEMU 9.2.2 - 3dfx (recommended and for Vectras VM 4.1.1+):
```bash
apk add bash && curl -H 'Cache-Control: no-cache' -o setup.sh https://raw.githubusercontent.com/AnBui2004/Vectras-VM-Emu-Android/refs/heads/master/qemu/9.2.2/upgrade.sh && chmod +rwx setup.sh && ./setup.sh; rm setup.sh
```

QEMU 7.2.22 - 3dfx (requires Vectras VM 4.2.2+):
```bash
apk add bash && curl -H 'Cache-Control: no-cache' -o setup.sh https://raw.githubusercontent.com/AnBui2004/Vectras-VM-Emu-Android/refs/heads/master/qemu/7.2.22/upgrade.sh && chmod +rwx setup.sh && ./setup.sh; rm setup.sh
```

## ⚙️ Bootstraps
QEMU 11.0.0 (for Vectras VM 4.0.8+):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.0.8/base-vectras-vm-arm64-v8a.tar.gz)
- [See more at the Internet Archive](https://archive.org/details/qemu-11-0-0-for-vectras-vm-nbab)

QEMU 9.2.4 - 3dfx (only for Vectras VM 3.5.0):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-9.2.2-3dfx-july-2026-vectras-vm-arm64-v8a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-9.2.2-3dfx-july-2026-vectras-vm-x86_64.tar.gz)
- [See them at the Internet Archive](https://archive.org/details/qemu-9-2-4-3dfx-for-vectras-vm-nbab)

QEMU 9.2.2 - 3dfx (recommended and for Vectras VM 4.4.7+):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.5.1/base-genegic-nosve-vectras-vm-arm64-v8a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.5.1/base-generic-vectras-vm-x86_64.tar.gz)
- [See them at the Internet Archive](https://archive.org/details/qemu-9-2-2-3dfx-for-vectras-vm-nbab)

QEMU 9.2.2 - 3dfx (recommended and for Vectras VM 4.1.1+):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.1.1/base-may-2026-vectras-vm-arm64-v8a.tar.gz)
- [See them at the Internet Archive](https://archive.org/details/qemu-9-2-2-3dfx-for-vectras-vm-nbab)

QEMU 9.2.2 - 3dfx (recommended and for Vectras VM 3.5.1+):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.5.1/base-genegic-nosve-vectras-vm-arm64-v8a.tar.gz)
- [For Android ARM (32-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.5.4/base-vectras-vm-armeabi-v7a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.5.1/base-generic-vectras-vm-x86_64.tar.gz)
- [For Android x86 (32-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.5.4/base-vectras-vm-x86.tar.gz)
- [See them at the Internet Archive (64-bit)](https://archive.org/details/qemu-9-2-2-3dfx-for-vectras-vm-nbab)
- [See them at the Internet Archive (32-bit)](https://archive.org/details/qemu-9-2-2-for-vectras-vm-nbab)

QEMU 9.2.2 - 3dfx (for Vectras VM 3.2.9 - 3.4.9):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.2.9/base-vectras-vm-arm64-v8a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.2.9/base-vectras-vm-x86_64.tar.gz)
- [See them at the Internet Archive](https://archive.org/details/qemu-9-2-2-3dfx-for-vectras-vm-nbab)

QEMU 8.2.0 - 3dfx (only for Vectras VM 2.9.5):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.2.9/vectras-vm-arm64-v8a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/3.2.9/vectras-vm-x86_64.tar.gz)
- [See them at the Internet Archive](https://archive.org/details/vectras-vm-x86_64.tar_202603nbab)

QEMU 7.2.22 - 3dfx (for Vectras VM 4.4.7+):
- [For Android ARM (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-3dfx-july-2026-vectras-vm-arm64-v8a.tar.gz)
- [For Android ARM (32-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-july-2026-vectras-vm-armeabi-v7a.tar.gz)
- [For Android x86 (64-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-3dfx-july-2026-vectras-vm-x86_64.tar.gz)
- [For Android x86 (32-bit)](https://github.com/AnBui2004/Vectras-VM-Emu-Android/releases/download/4.4.7/base-qemu-7.2.22-july-2026-vectras-vm-x86.tar.gz)
- [See more at the Internet Archive (64-bit)](https://archive.org/details/qemu-7-2-22-for-vectras-vm-nbab)
- [See more at the Internet Archive (32-bit)](https://archive.org/details/qemu-7-2-22-for-vectras-vm-nbab0)

QEMU 7.2.22 - 3dfx (for Vectras VM 4.2.2+):
- [For Android ARM (64-bit)](https://archive.org/download/qemu-7-2-22-for-vectras-vm-nbab/base-june-2026-vectras-vm-arm64-v8a.tar.gz)
- [See more at the Internet Archive](https://archive.org/details/qemu-7-2-22-for-vectras-vm-nbab)

## 💽 3Dfx Wrappers

### New:
- [For i686](https://github.com/AnBui2004/Vectras-VM-Emu-Android/blob/master/3dfx/4.1.1%2B/3dfx-wrappers-i686.iso)
- [For i586](https://github.com/AnBui2004/Vectras-VM-Emu-Android/blob/master/3dfx/4.1.1%2B/3dfx-wrappers-i586.iso)

### Old:
- [For QEMU 9.2.x - 3dfx](https://github.com/AnBui2004/Vectras-VM-Emu-Android/blob/master/3dfx/old/3dfx-wrappers-3.5.0.iso)
- [For QEMU 8.2.0 - 3dfx](https://github.com/AnBui2004/Vectras-VM-Emu-Android/blob/master/3dfx/old/3dfx-wrappers-2.9.5.iso)

## 🔬 For developers
[Build libXlorie.so](termux-x11/README.md)

[ico-telegram]: https://img.shields.io/badge/Telegram-2CA5E0?logo=telegram&logoColor=white
[link-discord]: https://discord.gg/t8TACrKSk7
[link-telegram]: https://t.me/vectras_os