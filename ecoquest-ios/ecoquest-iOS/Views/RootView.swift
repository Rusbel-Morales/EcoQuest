//
//  RootView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/16/24.
//

import SwiftUI

enum AppRoute: Hashable {
    case leaderboard
    case missions
    case stats
    case achievements
    case emissions
//    case shareExperience
}

struct RootView: View {
    @StateObject private var authViewModel = AuthViewModel()
    @State private var path: NavigationPath = NavigationPath()
    
    var body: some View {
        NavigationStack(path: $path) {
            Group {
                if authViewModel.isSignedIn {
                    BottomBar() // Show the main TabView when signed in
                        .environmentObject(authViewModel)
                } else {
                    AuthView(path: $path, viewModel: authViewModel) // Show AuthView when not signed in
                }
            }
            .navigationDestination(for: AppRoute.self) { route in
                switch route {
                case .missions:
                    Misiones(path: $path)
                case .leaderboard:
                    LeaderboardView(path: $path)
                case .stats:
                    EstadisticasView(path: $path)
                case .achievements:
                    EstadisticasDetailView(path: $path)
                case .emissions:
                    EmissionsView(path: $path)
//                case .shareExperience:
//                    MissionShareView(path: $path)
                }
            }
        }
        .environmentObject(authViewModel)
    }
}
