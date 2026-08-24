# 1. Install sd
This is what will help patch it.
```bash
sudo apt update && sudo apt install cargo -y
cargo install sd
echo 'export PATH="$HOME/.cargo/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```
# 2. Clone Termux:X11
Clone Termux:X11 to your PC.
```bash
git clone --recurse-submodules https://github.com/termux/termux-x11
```
# 3. Patch
The reason we use `sd` instead of `patch` is that it is not complicated.


```bash
cd termux-x11/lorie/src/main/cpp
# Replace them so it works with Vectras VM.
sd '_com_termux_x11_' '_com_vectras_vm_x11_' lorie/cmdentrypoint.cpp
sd '/data/data/com.termux/' '/data/data/com.vectras.vm/' lorie/cmdentrypoint.cpp
sd 'com/termux/x11/MainActivity' 'com/vectras/vm/x11/X11Activity' lorie/activity.cpp
sd 'com/termux/x11/LorieView' 'com/vectras/vm/x11/LorieView' lorie/activity.cpp
sd 'com.termux.x11' 'com.vectras.vm' lorie/activity.cpp
sd 'com/termux/x11/LorieView' 'com/vectras/vm/x11/LorieView' lorie/renderer.cpp
# Force the socket to open at `/data/data/com.vectras.vm/files/usr/tmp` instead of `/data/data/com.vectras.vm/cache` so that the Linux system running inside proot can see it.
sd '// adb sets TMPDIR to /data/local/tmp which is pretty useless.' '\n    setenv("TMPDIR", "/data/data/com.vectras.vm/files/usr/tmp", 1);\n\n    // adb sets TMPDIR to /data/local/tmp which is pretty useless.' lorie/cmdentrypoint.cpp
# Disable abstract sockets to avoid conflicts with other X11 displays, such as Termux:X11 app.
sd '#define HAVE_ABSTRACT_SOCKETS' '// #define HAVE_ABSTRACT_SOCKETS' libxtrans/Xtranssock.c
```
# 4. Build
Now, build the `lorie-app` in Android Studio. Once the build is complete, the `libXlorie.so` files will be located at `termux-x11/lorie/build/intermediates/cxx/RelWithDebInfo/*/obj`.
# 5. Finish
You should strip it to reduce its size.
```bash
~/Android/Sdk/ndk/*/toolchains/llvm/prebuilt/linux-x86_64/bin/llvm-strip --strip-unneeded termux-x11/lorie/build/intermediates/cxx/RelWithDebInfo/*/obj/*/libXlorie.so
```
And now, copy them to the Vectras VM at `app/src/jniLibs`, and it is ready to use.