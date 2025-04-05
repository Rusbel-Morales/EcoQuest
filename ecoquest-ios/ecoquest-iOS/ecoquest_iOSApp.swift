//
//  ecoquest_iOSApp.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 9/18/24.
//

import SwiftUI
import FirebaseCore
import FirebaseAuth
import GoogleSignIn

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()
      
      // Customizing Tab Bar Appearance
      let tabBarAppearance = UITabBarAppearance()
      tabBarAppearance.configureWithOpaqueBackground()
      tabBarAppearance.backgroundColor = UIColor(red: 190/255, green: 220/255, blue: 116/255, alpha: 1)
      tabBarAppearance.stackedLayoutAppearance.selected.iconColor = .darkGray
      tabBarAppearance.stackedLayoutAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor.darkGray]
      tabBarAppearance.stackedLayoutAppearance.normal.iconColor = .black
      tabBarAppearance.stackedLayoutAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor.black]
      
      UITabBar.appearance().standardAppearance = tabBarAppearance
      UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance

    return true
  }
    
    func application(_ app: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
      return GIDSignIn.sharedInstance.handle(url)
    }
}

@main
struct ecoquest_iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @StateObject private var authViewModel = AuthViewModel()
    
    var body: some Scene {
        WindowGroup {
            RootView()
                .environmentObject(authViewModel)
                .onOpenURL { url in
                  GIDSignIn.sharedInstance.handle(url)
                }
                .onAppear {
                  GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
                    // Check if `user` exists; otherwise, do something with `error`
                      if let _ = user {
                          // User is signed in
                          authViewModel.isSignedIn = true
                          authViewModel.userId = user?.userID ?? "Unknown User"
                      } else if let error = error {
                          print("Error restoring previous sign-in: \(error.localizedDescription)")
                      }
                  }
        }
        }
    }
}
