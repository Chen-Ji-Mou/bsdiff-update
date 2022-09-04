## 前置环境搭建
- Windows
  - adb
- Mac
  - brew
  - adb
- Android Studio
  - ndk (version: 25.1.8937393)
  - cmake (version: 3.22.1)

## 食用方法
1. 使用数据线将手机连接电脑 / 启动一个 Android 模拟器 (一定要先连接设备再运行脚本, 否则脚本会出错)
2. 运行 build 脚本 (如果是 windows 系统就运行 build_windows, 根据系统运行对应脚本)
3. 打开 original_app, 点击 install new version 按钮

## 脚本说明
1. build 脚本会将 original_app 和 new_app 自动打 release 包, 并通过 bsdiff 生成对应的差分文件 patch, 一并输出在 /outputs 目录下
2. 通过 FTP 上传 patch 差分文件至云服务器
3. 通过 adb 自动安装 original_app 包至连接电脑的 Android 设备

## 错误说明
- build 脚本显示 "远端主机关闭连接"
  1. 使用 ping 指令连接 FTP 远端主机检查是否能连接成功 (远端主机IP: 43.143.169.6), 如果 ping 不成功请检查防火墙设置和网络设置
  2. 如果能 ping 成功, 检查所用网络是否为局域网, 如果为局域网请切换网络重试
- build 脚本显示 "500 Illegal PORT command. 425 Use PORT or PASV first."
  1. 检查是否在使用 VPN, 如果在使用 VPN 请关闭 VPN 后重试
- build 脚本显示 "error: no devices/emulators found"
  1. 打开本地终端运行 adb devices 指令查看本机是否有已连接的 Android 设备, 如果没有请至少连接上一台 Android 设备后重试
- build 脚本显示 "Android Gradle plugin requires Java 11 to run. You are currently using Java xxx"
  1. 由于 demo 中使用的 Gradle 版本为 7.3.3, 请检查本机环境变量中设置的 JDK 版本是多少, 如果 JDK 版本低于 11 请升级 JDK 版本或降低 Gradle 版本

## 其他说明
- bsdiff_android 是经过修改后可导入项目的 bsdiff 源码, 可直接 cp 至自己已有的项目中进行编译, 并按照 demo 中的实现在自己的已有项目中实现增量更新功能
- bsdiff_windows 是已经编译好的能在 windows 系统中运行的 bsdiff, 主要用于配合 build_windows 脚本运行
