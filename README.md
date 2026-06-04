# Summary of Java Screen Capture Project
## Overview
This project contains 2 Java source codes and 1 README document, implementing GUI-based screen capture based on Java AWT & Swing for technical practice. Two versions of capture programs are provided with different shortcut rules and configurations.

## 1. ScreenCaptureApp.java
### Features
- Graphical UI with topmost checkbox, capture button and custom save folder selector.
- Hotkey `Ctrl+Shift+S` works **only when program window is focused**.
- Screenshots default saved to `User/Pictures/java-screenshots`; directory editable manually.
- Auto preview captured image with scaled display, save file named by timestamp.
- Launches UI via Swing event dispatch thread for thread safety.

## 2. ScreenCaptureTool.java
### Features
- Minimal UI with capture button and topmost toggle button.
- System-wide global `Ctrl+Shift+S` shortcut via KeyboardFocusManager (no window focus required).
- Capture preview supports original-size scrollable view to avoid blank preview.
- Pictures auto-saved in working directory named `IH_Snap_[timestamp].png`.
- Main window hides temporarily during capture for full-screen snapshot.

## 3. README.md Instructions
- Basic compile & run commands for original code without extra dependencies.
- Note: Built-in shortcut only effective inside app window; **JNativeHook jar required for true global hotkey**.
- Supply compilation syntax with JNativeHook dependency, differentiate classpath separator (`;` for Windows, `:` for macOS/Linux).
- Optional upgrade suggestions: region capture, delayed snapshot, tray icon, copy image to clipboard.

## Core Differences Between Two Programs
| Item | ScreenCaptureApp | ScreenCaptureTool |
| ---- | ---- | ---- |
| Hotkey Range | Window-focused only | System global |
| Save Path | Customizable folder | Fixed program directory |
| Preview | Auto scaled thumbnail | Original size with scroll bar |

## Technical Info
- Core API: java.awt.Robot for screen capture, ImageIO for PNG storage.
- No third-party jars for basic use; JNativeHook optional for stable global keyboard hook.
