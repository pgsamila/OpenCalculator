# Open Calculator

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)

**Open Calculator** is a premium, open-source calculator app built entirely with modern Android technologies. It combines a beautiful "Glassmorphism" UI with powerful scientific features, privacy-focused design, and a robust architecture.

Whether you're a user looking for a better calculator or a developer wanting to learn Jetpack Compose, Open Calculator is for you.

---

## ✨ Features

- **🎨 Premium UI**: Sleek glassmorphism design with neon accents and subtle gradients.
- **📱 Adaptive Layout**: Automatically adjusts between Basic and Scientific modes based on screen size and expansion.
- **🌗 Theme Support**: Fully optimized Light and Dark modes, plus a "System Default" option.
- **🧮 Scientific Functions**:
  - Trigonometry (sin, cos, tan) with Degree/Radian toggle.
  - Logarithms (log, ln) and constants (e, π).
  - Powers, Roots, and Factorials.
- **📜 History**: Interactive calculation history that lets you reuse past results.
- **🔒 Privacy First**: No ads, no tracking, no unnecessary permissions.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Design System**: [Material Design 3](https://m3.material.io/)
- **Architecture**: MVVM (Model-View-ViewModel)
- **State Management**: `StateFlow` & `ViewModel`
- **Testing**:
  - Unit Tests: JUnit 4
  - UI Tests: Espresso & Compose Test Rule

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer.
- JDK 17 or higher.

### Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/pgsamila/OpenCalculator.git
    cd OpenCalculator
    ```

2.  **Open in Android Studio**:
    - Launch Android Studio.
    - Select "Open" and navigate to the cloned directory.

3.  **Sync Project**:
    - Allow Gradle to sync and download dependencies.

4.  **Run the App**:
    - Connect an Android device or start an emulator.
    - Click the **Run** button (▶️) or press `Shift + F10`.

## 🧪 Running Tests

### Unit Tests
Run local unit tests to verify logic (Expression Evaluator, ViewModel):
```bash
./gradlew testDebugUnitTest
```

### Instrumented Tests
Run UI tests on a connected device/emulator:
```bash
./gradlew connectedAndroidTest
```

## 🤝 Contributing

Contributions are welcome! If you'd like to improve Open Calculator, please follow these steps:

1.  **Fork the Project**.
2.  **Create your Feature Branch** (`git checkout -b feature/AmazingFeature`).
3.  **Commit your Changes** (`git commit -m 'Add some AmazingFeature'`).
4.  **Push to the Branch** (`git push origin feature/AmazingFeature`).
5.  **Open a Pull Request**.

Please ensure your code follows the existing style and all tests pass.

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📧 Contact

Project Link: [https://github.com/pgsamila/OpenCalculator](https://github.com/pgsamila/OpenCalculator)
