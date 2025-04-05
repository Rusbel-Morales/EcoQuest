//
//  EmissionsView.swift
//  ecoquest-iOS
//
//  Created by Administrador on 03/11/24.
//

import SwiftUI

struct EmissionsView: View {
    @Binding var path: NavigationPath
    @EnvironmentObject var authViewModel: AuthViewModel
    @StateObject private var xpViewModel: XpBarViewModel = XpBarViewModel() // Nuevo viewModel para obtener misPuntos
    @State private var isShareModalPresented = false

    var body: some View {
        ZStack {
            VStack {
                TopBar(isShareModalPresented: $isShareModalPresented)
                    .padding()
               
                Text("Gracias a tus contribuciones...")
                    .font(.headline)
                    .multilineTextAlignment(.center)
               
                // Texto central de "Evitamos x Kg"
                VStack {
                    Text("Evitamos")
                        .font(.title2)
                        .fontWeight(.semibold)
                        .multilineTextAlignment(.center)

                    HStack {
                        // Conversion mejorada de `misPuntos` a un valor de emisiones
                        let emisiones = Double(xpViewModel.xpBarData?.misPuntos ?? 0) * 0.01
                       
                        Text(String(format: "%.2f", emisiones))
                            .font(.system(size: 64))
                            .fontWeight(.bold)
                            .multilineTextAlignment(.center)
                       
                        Text(" Kg")
                            .font(.system(size: 32))
                            .fontWeight(.bold)
                            .multilineTextAlignment(.trailing)
                    }
                   
                    Text("De emisiones de CO2 a la atmósfera")
                        .font(.title2)
                        .multilineTextAlignment(.center)
                }
               
                Spacer()
               
                ZStack {
                    Image("carretera")
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(maxWidth: .infinity)
                        .clipped()
                   
                    VStack {
                        Spacer()
                            .frame(height: 350)
                    }
                    .frame(width: 410, height: 405)
                }
            }
        }
        .overlay(
            Group {
                if self.isShareModalPresented {
                    Color.black.opacity(0.5)
                        .edgesIgnoringSafeArea(.all)
                    InviteFriendsModalView(isPresented: $isShareModalPresented)
                }
            }
        )
        .onAppear {
            xpViewModel.fetchXpBarData(userId: authViewModel.userId)
        }
    }
}

#Preview {
    EmissionsView(path: .constant(NavigationPath()))
}
