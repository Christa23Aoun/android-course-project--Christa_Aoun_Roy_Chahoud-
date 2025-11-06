Hug Your Mug  
Android Course Project — Coffee Shop Companion App  
Team Members: Christa-Maria Aoun and Roy Chahoud  

App Description  
Hug Your Mug is a coffee shop companion application that enhances the café experience through convenience and personalization.  
Users can browse a digital coffee menu, add new drinks, and manage their list of coffees.  
All information is stored locally using the Room database.  
The application follows the MVVM architecture, ensuring a clear separation between the user interface, business logic, and data management layers.  
The project is designed to be extendable in the future to include AI-based coffee recommendations, loyalty tracking, and online ordering.

 Main Features  
- Digital coffee menu using RecyclerView.  
- Add new drinks through a dialog.  
- Swipe to delete items.  
- MVVM architecture with ViewModel and LiveData for real-time updates.  
- Local persistence using Room database.  
- Toolbar with menu options to add sample coffees and clear all.  
- Code structure allows future integration with networking and background tasks.

The project follows the MVVM (Model–View–ViewModel) pattern
com.example.hugyourmug
│
├── data/ # Entities, DAO, Repository, and Database classes
│ ├── AppDatabase.kt
│ ├── Coffee.kt
│ ├── CoffeeRepository.kt
│ └── DrinkDao.kt
│
├── viewmodel/ # ViewModel containing business logic
│ └── CoffeeViewModel.kt
│
├── ui/ # RecyclerView Adapter and layouts
│ └── CoffeeAdapter.kt
│
├── res/layout/ # activity_main.xml, dialog_add_coffee.xml, item_coffee.xml
└── MainActivity.kt # Main screen with toolbar, list, and add button


Data Flow:  
MainActivity → CoffeeViewModel → CoffeeRepository → Room Database  
Room updates are observed by LiveData, which updates the UI automatically.

 Setup Instructions  

 Prerequisites  
- Android Studio Koala or newer  
- JDK 17  
- Gradle Plugin 8.5.2 or newer  
- Kotlin 1.9.24 or newer  

How to Run  
1. Clone the repository:  
   ```bash
   git clone https://github.com/Christa23Aoun/android-course-project--Christa_Aoun_Roy_Chahoud-.git
Open the project in Android Studio.
Wait for Gradle sync to complete.
Click Run to launch the application on an emulator or device.
Use the floating action button to add coffees.
Swipe an item to delete it.
Open the menu (three dots) to add sample coffees or clear all.
