//
//  TopBar.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/14/24.
//

import SwiftUI

struct TopBar: View {
    @Binding var isShareModalPresented: Bool
    
    var body: some View {
        HStack(spacing: 0) { // Set spacing to 0
            Button(action: {
                isShareModalPresented = true
            }) {
                ZStack {
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color(red: 162/255, green: 202/255, blue: 113/255))
                        .frame(width: 40, height: 40)
                    Image(systemName: "person.badge.plus")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 22, height: 22)
                        .foregroundColor(.black)
                }
                .shadow(radius: 3)
            }
            
            Spacer()
            
            XPBar()
                .frame(width: 220, alignment: .trailing) // Adjust width as needed
        }
        .frame(width: .infinity)
        .padding(.horizontal) // Removed the explicit 16 value to use system default
        .padding(.vertical, 8)
    }
}

#Preview {
    TopBar(isShareModalPresented: .constant(false))
}
