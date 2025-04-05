//
//  MissionDescriptionModalView.swift
//  ecoquest-iOS
//
//  Created by Administrador on 09/10/24.
//

import SwiftUI
import AlertToast

struct MissionDescriptionModalView: View {
    @Binding var isPresented: Bool
    @Binding var mission: Mission?
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.4)
                .edgesIgnoringSafeArea(.all)
            
            VStack(spacing: 20) {
                // Close Button
                HStack {
                    Spacer()
                    Button(action: {
                        isPresented = false
                        mission = nil
                    }) {
                        Image(systemName: "xmark.circle.fill")
                            .resizable()
                            .frame(width: 24, height: 24)
                            .foregroundColor(.gray.opacity(0.8))
                    }
                }
                .padding(.trailing, 10)
                
                // Title and Icon
                HStack(spacing: 12) {
                    Image(systemName: "star.fill")
                        .foregroundColor(.yellow)
                        .font(.system(size: 28))
                    
                    Text(mission?.titulo ?? "Título de la Misión")
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(.primary)
                        .multilineTextAlignment(.center)
                        .lineLimit(2)
                        .padding(.horizontal, 8)
                }
                
                // Description
                Text(mission?.descripcion ?? "Descripción de la misión.")
                    .font(.body)
                    .foregroundColor(.primary.opacity(0.8))
                    .multilineTextAlignment(.leading)
                    .padding(.horizontal, 10)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Spacer()
            }
            .padding()
            .background(Color.white)
            .cornerRadius(20)
            .frame(maxWidth: 320, maxHeight: 380)
            .shadow(color: Color.black.opacity(0.2), radius: 15, x: 0, y: 5)
        }
    }
}
