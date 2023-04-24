package com.shaderock.lunch.backend.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

//  public BufferedImage resizeByWidthAndCropToFitHeight(BufferedImage originalImage, int targetWidth,
//      int targetHeight) {
//    // First, resize the image to the target width while maintaining aspect ratio
//    BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY,
//        Scalr.Mode.FIT_TO_WIDTH, targetWidth);
//
//    // Check if the requested height is higher than the original image height after resizing
//    if (targetHeight > resizedImage.getHeight()) {
//      // Return the resized image without cropping
//      return resizedImage;
//    } else {
//      // Calculate the amount to crop from the top and bottom
//      int cropAmount = (resizedImage.getHeight() - targetHeight) / 2;
//
//      // Crop the image
//      return Scalr.crop(resizedImage, 0, cropAmount, resizedImage.getWidth(), targetHeight);
//    }
//  }

  private static BufferedImage getBufferedImage(byte[] originalImageBytes) throws IOException {
    InputStream is = new ByteArrayInputStream(originalImageBytes);
    return ImageIO.read(is);
  }

  public BufferedImage resizeByWidthAndCropToFitHeight(BufferedImage originalImage, int targetWidth,
      int targetHeight) {
    // First, resize the image to the target width while maintaining aspect ratio
    BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY,
        Scalr.Mode.FIT_TO_WIDTH, targetWidth);

    // Check if the requested height is higher than the original image height after resizing
    if (targetHeight > resizedImage.getHeight()) {
      // Return the resized image without cropping
      return resizedImage;
    } else {
      // Crop the image to fit the target height
      return cropImageIfHeightIsMoreThanTarget(resizedImage, targetHeight);
    }
  }

  public BufferedImage createThumbnail(BufferedImage originalImage) {
    // Create a thumbnail of the original image with a width of 150 pixels
    // while maintaining the aspect ratio
    return Scalr.resize(originalImage, 100);
  }

  public BufferedImage cropImageIfHeightIsMoreThanTarget(BufferedImage originalImage,
      int targetHeight) {
    // Check if the original image height is higher than the target height
    if (originalImage.getHeight() > targetHeight) {
      // Calculate the amount to crop from the top and bottom
      int cropAmount = (originalImage.getHeight() - targetHeight) / 2;

      // Crop the image
      return Scalr.crop(originalImage, 0, cropAmount, originalImage.getWidth(), targetHeight);
    } else {
      // Return the original image without cropping
      return originalImage;
    }
  }

  @SneakyThrows
  public byte[] resizeByWidthAndCropToFitHeight(byte[] originalImageBytes, int targetWidth,
      int targetHeight) {
    BufferedImage bufferedImage = getBufferedImage(originalImageBytes);
    BufferedImage resized = resizeByWidthAndCropToFitHeight(bufferedImage, targetWidth,
        targetHeight);
    return bufferedImageToByteArray(resized, "JPEG");
  }

  @SneakyThrows
  public byte[] resizeToThumbnail(byte[] logo) {
    BufferedImage bufferedImage = getBufferedImage(logo);
    BufferedImage thumbnail = createThumbnail(bufferedImage);
    return bufferedImageToByteArray(thumbnail, "JPEG");
  }

  @SneakyThrows
  public byte[] bufferedImageToByteArray(BufferedImage image, String formatName) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, formatName, outputStream);
    return outputStream.toByteArray();
  }


}
