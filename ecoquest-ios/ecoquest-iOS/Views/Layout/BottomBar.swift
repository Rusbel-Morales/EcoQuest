//
//  BottomBar.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/14/24.
//

import SwiftUI

struct BottomBar: View {
    @State private var path: NavigationPath = NavigationPath()
    
    var body: some View {
        TabView {
            Misiones(path: $path)
                .tabItem {
                    Label("Misiones", systemImage: "flag.fill")
                }
            
            EmissionsView(path: $path)
                .tabItem {
                    Label("Impacto", systemImage: "leaf.fill")
                }
            
            EstadisticasView(path: $path)
                .tabItem {
                    Label("Estadisticas", systemImage: "chart.bar.fill")
                }
            
            EstadisticasDetailView(path: $path)
                .tabItem {
                    Label("Logros", systemImage: "trophy.fill")
                }
            
            LeaderboardView(path: $path)
                .tabItem {
                    Label("Clasificación", systemImage: "list.number")
                }
        }
        .accentColor(Color(.black))
    }
}
