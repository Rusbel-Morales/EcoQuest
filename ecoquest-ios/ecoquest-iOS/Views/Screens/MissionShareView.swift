import SwiftUI
import PhotosUI

struct MissionShareView: View {
    @Environment(\.presentationMode) var presentationMode
    @Binding var completedMission: Mission?
    @State private var processedImage: Image?
    @State private var selectedItem: PhotosPickerItem?
    @State private var descriptionText: String = ""

    var body: some View {
        VStack {
            Spacer()

            // Ecoquest Logo
            Image("logo-ecoquest")
                .resizable()
                .frame(height: 90)
                .cornerRadius(15)

            Spacer().frame(height: 70)

            // ZStack to overlay the mission title
            ZStack(alignment: .top) {
                // Main Content
                VStack(spacing: 15) {
                    // Back Button
                    HStack {
                        Button(action: {
                            presentationMode.wrappedValue.dismiss()
                        }) {
                            Image(systemName: "arrow.backward")
                                .foregroundColor(Color.black)
                        }
                        Spacer()
                    }

                    // Description Header
                    Text("Cuenta tu experiencia con esta misión")
                        .font(.system(size: 18, weight: .bold))
                        .frame(maxWidth: .infinity, alignment: .leading)

                    // Description TextEditor
                    TextEditor(text: $descriptionText)
                        .padding()
                        .background(Color.white)
                        .cornerRadius(10)
                        .frame(height: 145)
                        .shadow(radius: 2)

                    // Photo Picker and Display
                    PhotosPicker(selection: $selectedItem) {
                        if let processedImage {
                            processedImage
                                .resizable()
                                .scaledToFit()
                        } else {
                            VStack {
                                Text("No picture selected")
                                    .foregroundColor(.gray)
                                Image(systemName: "photo.badge.plus")
                                    .font(.largeTitle)
                            }
                        }
                    }
                    .onChange(of: selectedItem, loadImage)
                    .buttonStyle(.plain)
                }
                .padding()
                .background(Color(red: 246/255, green: 233/255, blue: 107/255))
                .cornerRadius(15)
                .padding(.horizontal)

                // Mission Title overlayed at the top of the VStack
                HStack {
                    Image(systemName: "target")
                        .resizable()
                        .frame(width: 30, height: 30)
                    Text(completedMission?.titulo ?? "")
                        .font(.system(size: 20, weight: .bold))
                }
                .padding()
                .background(Color(red: 162/255, green: 202/255, blue: 113/255))
                .foregroundColor(.black)
                .cornerRadius(10)
                .shadow(radius: 3)
                .offset(y: -30) // Adjust offset to position the title as desired
            }

            Spacer()

            // Conditional ShareLink
            if let imageToShare = processedImage {
                ShareLink(
                    item: descriptionText,
                    preview: SharePreview(descriptionText, image: imageToShare)
                ) {
                    ShareButtonContent()
                }
            } else {
                ShareLink(item: descriptionText) {
                    ShareButtonContent()
                }
            }

            Spacer()
        }
        .background(Color.white)
        .edgesIgnoringSafeArea(.bottom)
    }

    // Function to load the selected image
    private func loadImage() {
        Task {
            guard let data = try? await selectedItem?.loadTransferable(type: Data.self),
                  let uiImage = UIImage(data: data) else { return }

            processedImage = Image(uiImage: uiImage) // Set the image for display
        }
    }
}

// Extracted button content for reusability
struct ShareButtonContent: View {
    var body: some View {
        HStack {
            Image(systemName: "square.and.arrow.up")
            Text("Compartir")
                .fontWeight(.bold)
        }
        .font(.system(size: 20, weight: .bold))
        .padding()
        .frame(width: 200)
        .background(Color(red: 162/255, green: 202/255, blue: 113/255))
        .foregroundColor(.black)
        .cornerRadius(10)
        .shadow(radius: 3)
    }
}
