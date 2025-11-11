# MathSnap - Snap into Maths Mastery 🎯

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg" alt="API">
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange.svg" alt="Architecture">
</p>

**MathSnap** is a premium, gamified mathematics learning app that makes practicing math fun, engaging, and habit-forming for all ages. Built with modern Android development best practices using Jetpack Compose, Material 3, and clean architecture.

## ✨ Features

### Core Functionality
- **Quick Math Practice**: Addition, subtraction, multiplication, and division
- **Progressive Difficulty**: Easy → Medium → Hard → Expert levels
- **Daily Challenges**: Fresh problems every day to maintain streaks
- **Offline-First**: Full functionality without internet using Room database
- **Voice Input**: Answer problems using speech recognition
- **Performance Tracking**: Detailed statistics and progress visualization

### Gamification
- **Streak System**: Maintain daily practice streaks with fire emoji indicators
- **Badge Collection**: 15+ unlockable badges for achievements
- **Timed Challenges**: Speed math mode for competitive practice
- **Rewards System**: Earn rewards for consistent practice
- **Shareable Achievements**: Export "Math Cards" with your stats

### User Experience
- **Material 3 Design**: Premium, modern UI following Google's latest design guidelines
- **Dark/Light Themes**: Automatic or manual theme switching
- **Kid-Friendly Mode**: Larger buttons, playful interface, avatar selection
- **Smooth Animations**: Delightful transitions and feedback animations
- **Instant Feedback**: Real-time answer validation with hints

### Monetization
- **Free Tier**: Full basic functionality with ads
- **Premium Tier**: Ad-free experience + advanced modules
- **Math Mastery Pack**: AI-generated problems + personalized learning paths

## 🏗️ Architecture

MathSnap follows **Clean Architecture** principles with **MVVM** pattern and is built with a modular, scalable structure.

```
app/
├── data/                       # Data layer
│   ├── local/                  # Room database
│   │   ├── dao/               # Database access objects
│   │   ├── entity/            # Room entities
│   │   └── MathSnapDatabase   # Database setup
│   └── repository/            # Repository implementations
│
├── domain/                     # Domain layer (Business Logic)
│   ├── model/                 # Domain models
│   ├── repository/            # Repository interfaces
│   └── usecase/               # Business use cases
│
├── ui/                        # Presentation layer
│   ├── screens/               # Feature screens
│   │   ├── home/             # Home screen with daily challenge
│   │   ├── practice/         # Practice mode screen
│   │   ├── progress/         # Progress dashboard
│   │   └── settings/         # Settings screen
│   ├── components/            # Reusable UI components
│   ├── navigation/            # Navigation setup
│   ├── theme/                # Material 3 theming
│   └── MainActivity           # Main activity
│
├── di/                        # Dependency Injection
│   ├── DatabaseModule         # Database DI
│   └── RepositoryModule       # Repository DI
│
└── util/                      # Utilities
    ├── VoiceInputHelper       # Speech recognition
    └── AdManager              # AdMob integration
```

### Technology Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Design System** | Material 3 |
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Hilt |
| **Database** | Room |
| **Async** | Kotlin Coroutines + Flow |
| **Navigation** | Navigation Compose |
| **Voice Input** | Android SpeechRecognizer |
| **Ads** | AdMob |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 (Android 14) |

## 🎯 Key Components

### Domain Models
- **MathProblem**: Represents a math problem with operands, operation, and answer
- **UserProgress**: Tracks user statistics, streaks, and performance
- **Badge**: Achievement badges with unlock conditions
- **DailyChallenge**: Generated daily set of problems

### Use Cases
- **GenerateProblemUseCase**: Creates random problems based on difficulty
- **CheckAnswerUseCase**: Validates answers and provides hints
- **UpdateProgressUseCase**: Updates user statistics and streaks
- **CheckBadgesUseCase**: Evaluates and unlocks earned badges
- **GenerateDailyChallengeUseCase**: Creates daily challenge sets

### Repositories
- **ProgressRepository**: Manages user progress and attempt history
- **BadgeRepository**: Handles badge data and unlock logic
- **DailyChallengeRepository**: Manages daily challenges

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 17 or newer
- Android SDK with API 34
- Gradle 8.0+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/Math-Snap.git
cd Math-Snap
```

2. **Open in Android Studio**
- Open Android Studio
- Select "Open an Existing Project"
- Navigate to the cloned directory
- Wait for Gradle sync to complete

3. **Configure AdMob (Optional)**
- Replace test ad unit IDs in `AdManager.kt` and `AdBanner.kt`
- Update `AndroidManifest.xml` with your AdMob App ID

4. **Build and Run**
```bash
./gradlew assembleDebug
```
or use Android Studio's Run button

### Running Tests
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📱 App Flow

```
┌─────────────┐
│ Home Screen │ ──> Daily Challenge
│             │ ──> Quick Practice (4 operations)
│  - Streak   │ ──> Progress Dashboard
│  - Stats    │ ──> Settings
└─────────────┘
       │
       ▼
┌──────────────┐
│Practice Mode │ ──> Generate Problem
│              │ ──> Answer Input (Text/Voice)
│  - Problem   │ ──> Instant Feedback
│  - Timer     │ ──> Hints
│  - Streak    │ ──> Badge Unlocks
└──────────────┘
       │
       ▼
┌──────────────┐
│Progress View │ ──> Overall Stats
│              │ ──> Operation Breakdown
│  - Accuracy  │ ──> Earned Badges
│  - Speed     │ ──> Challenge History
└──────────────┘
```

## 🎮 Gamification System

### Streaks
- **Daily Practice Tracking**: Maintains streaks for consecutive practice days
- **Streak Recovery**: Grace period for missed days
- **Visual Indicators**: Fire emoji with count in UI

### Badges (15+ Available)
| Badge | Requirement | Icon |
|-------|------------|------|
| First Steps | Solve first problem | 🎯 |
| Week Warrior | 7-day streak | 🔥 |
| Month Master | 30-day streak | 🏆 |
| Streak Legend | 100-day streak | 👑 |
| Perfect 10 | 10 correct in a row | 💯 |
| Speed Demon | Solve in under 3 seconds | ⚡ |
| Century Club | 100 problems solved | 💯 |
| Thousand Club | 1000 problems solved | 🎖️ |
| Operation Masters | 100 correct per operation | ➕➖✖️➗ |
| Night Owl | Practice after 10 PM | 🦉 |
| Early Bird | Practice before 7 AM | 🐦 |

### Difficulty Progression
1. **Easy**: Single-digit numbers (1-10)
2. **Medium**: Double-digit numbers (10-50)
3. **Hard**: Larger numbers (50-100)
4. **Expert**: Complex problems (100-500)

## 📈 Viral Growth Hooks

### Built-in Viral Features

1. **Shareable Math Cards**
   - Beautiful achievement cards with user stats
   - Export as images with MathSnap branding
   - Social media integration (Twitter, Instagram, Facebook)
   - Hashtag: #MathSnapChallenge

2. **Leaderboards (Planned)**
   - Weekly/Monthly global rankings
   - Friend challenges
   - School/Class competitions
   - Encourage daily engagement

3. **Referral System (Planned)**
   - Invite friends for premium features
   - Both users get rewards
   - Track referral statistics

4. **Daily Challenge Sharing**
   - Share daily challenge with friends
   - Compare scores
   - Group challenges for families/classrooms

### Growth Strategies

**For Parents & Educators:**
- Free classroom licenses for teachers
- Progress reports for parents
- Curriculum alignment documentation
- Educational blog content

**For Kids:**
- Avatar customization unlocks
- Collectible badges and rewards
- Fun sound effects and animations
- Kid-safe, ad-appropriate content

**For Competitive Learners:**
- Speed challenges with time tracking
- Accuracy competitions
- Global/local leaderboards
- Achievement showcases

### Onboarding Flow

```
Welcome Screen
     ↓
Select Age Group
     ↓
Quick Tutorial (3 practice problems)
     ↓
Set Daily Goal
     ↓
Enable Notifications (Optional)
     ↓
Start First Challenge
```

**Key Onboarding Principles:**
- Show value within 30 seconds
- Let users solve problems immediately
- Gamify from the first interaction
- Collect minimal data upfront
- Celebrate small wins early

## 🎨 Design System

### Color Palette
- **Primary**: Purple (Material 3 Dynamic Color)
- **Addition**: Green (#4CAF50)
- **Subtraction**: Orange (#FF9800)
- **Multiplication**: Blue (#2196F3)
- **Division**: Purple (#9C27B0)

### Typography
- Display: Bold, 57sp - 36sp
- Headlines: Bold, 32sp - 24sp
- Title: SemiBold, 22sp - 16sp
- Body: Regular, 16sp - 14sp

### Components
- Cards: 12-24dp corner radius, elevated
- Buttons: Filled/Outlined with 12dp radius
- Input Fields: Outlined with 16dp radius
- Icons: Material Icons Extended

## 🔐 Privacy & Security

- **No Account Required**: Practice without sign-up
- **Local Data Storage**: All data stored on device
- **Optional Cloud Sync**: User-controlled backup
- **COPPA Compliant**: Safe for children
- **No Data Selling**: User data never sold or shared
- **Transparent Privacy Policy**: Clear data usage explanation

## 🚧 Roadmap

### Version 1.1 (Planned)
- [ ] Fractions module
- [ ] Algebra basics
- [ ] Math puzzles and word problems
- [ ] Cloud sync for progress
- [ ] Parent/Teacher dashboard

### Version 1.2 (Planned)
- [ ] AI-generated personalized problems
- [ ] Multi-player challenges
- [ ] Voice explanations for solutions
- [ ] Augmented reality math games
- [ ] Integration with Google Classroom

### Version 2.0 (Future)
- [ ] Advanced topics (Geometry, Calculus)
- [ ] Adaptive learning paths
- [ ] Video tutorials
- [ ] Live tutoring integration
- [ ] Certification system

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

### How to Contribute
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact & Support

- **Email**: support@mathsnap.app
- **Twitter**: [@MathSnapApp](https://twitter.com/mathsnapapp)
- **Website**: [www.mathsnap.app](https://www.mathsnap.app)
- **Discord**: [Join our community](https://discord.gg/mathsnap)

## 🙏 Acknowledgments

- Material 3 Design Guidelines
- Jetpack Compose community
- Android Architecture Components team
- All contributors and beta testers

---

**Made with ❤️ for math learners worldwide**

*MathSnap - Making Math Practice Fun, One Problem at a Time*
