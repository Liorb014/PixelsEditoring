package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.stream.IntStream;

public class Window extends JFrame {
    public Window() {
        try {
            URL jetImage = getClass().getClassLoader().getResource("pop.jpg");
            if (jetImage != null) {
                BufferedImage bufferedImage = ImageIO.read(jetImage);
                this.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
                JLabel label = new JLabel(new ImageIcon(xFilter()));
                label.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label.setIcon(new ImageIcon(xFilter()));
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });

                this.add(label);
            } else {
                System.out.println("cannot find");
            }
            this.setSize(256, 256);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public BufferedImage xFilter() {
        Random random = new Random();
        BufferedImage processed = new BufferedImage(256, 256, BufferedImage.TYPE_INT_BGR);
        int min = random.nextInt(8, 128);
        int max = min + 120;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                int red = min;
                int green = min;

                if (j > min && j < max) {
                    red=j;
                }
                if (j >= max) {
                    red = max;
                }
                if (i > min && i < max) {
                    green=i;
                }
                if (i >= max) {
                    green = max;
                }
                int blue = (red + green) / 2;
                Color color1 = new Color(red, green, blue);
                processed.setRGB(j, i, color1.getRGB());
            }
        }
        return processed;
    }

    public boolean isDark(Color color) {
        return color.getRed() < 150 && color.getBlue() < 150 && color.getGreen() < 150;
    }

    public boolean isRedier(Color color) {
        return color.getRed() + color.getBlue() + color.getGreen() > 150 && color.getGreen() < 50 && color.getBlue() < 50;
    }

    public BufferedImage mirroring(BufferedImage original) {
        BufferedImage processed = new BufferedImage(original.getWidth(), original.getHeight(), original.TYPE_INT_BGR);
        for (int i = 0; i < original.getHeight(); i++) {
            for (int j = 0; j < original.getWidth(); j++) {
                processed.setRGB(i, j, original.getRGB(original.getWidth() - 1, j));
            }
        }
        return processed;
    }

    public BufferedImage grayScale(BufferedImage original) {
        BufferedImage processed = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < original.getHeight(); i++) {
            for (int j = 0; j < original.getWidth(); j++) {
                Color color = new Color(original.getRGB(i, j));
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                int average = (red + green + blue) / 3;
                Color newColor = new Color(average, average, average);
                processed.setRGB(i, j, newColor.getRGB());
            }
        }
        return processed;
    }

    public BufferedImage shuffle(BufferedImage original) {
        BufferedImage processed = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < original.getHeight(); i++) {
            for (int j = 0; j < original.getWidth(); j++) {
                Color color = new Color(original.getRGB(i, j));
                Color pixelColorPlus = new Color(original.getRGB(i + 3, j + 3));

                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();

                Color newColor = new Color(red, blue, green);
                processed.setRGB(i, j, newColor.getRGB());
            }

        }
        return processed;
    }

    public BufferedImage locateEyes(BufferedImage origin) {
        BufferedImage processed = new BufferedImage(origin.getWidth(), origin.getHeight(), BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < origin.getHeight(); i++) {
            for (int j = 0; j < origin.getWidth(); j++) {
                Color color = new Color(origin.getRGB(j, i));
                if (isDark(color)) {
                    Color newColor = new Color(0, 0, 0);
                    processed.setRGB(j, i, newColor.getRGB());
                } else {
                    Color color1 = new Color(255, 255, 255);
                    processed.setRGB(j, i, color1.getRGB());
                }
            }
        }
        return processed;
    }


    public BufferedImage drawEdges(BufferedImage original) {
        BufferedImage output = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        IntStream.range(1, original.getWidth() - 1).forEach(x -> {
            IntStream.range(1, original.getHeight() - 1).forEach(y -> {
                Color current = new Color(original.getRGB(x, y));
                Color north = new Color(original.getRGB(x, y + 1));
                Color south = new Color(original.getRGB(x, y - 1));
                Color east = new Color(original.getRGB(x - 1, y));
                Color west = new Color(original.getRGB(x + 1, y));
                if (isDifferent(current, north) ||
//                        isDifferent(current, south) ||
//                        isDifferent(current, east) ||
                        isDifferent(current, west)) {
                    output.setRGB(x, y, Color.BLUE.getRGB());
                } else {
                    output.setRGB(x, y, original.getRGB(x, y));

                }
            });
        });
        return output;
    }

    private boolean isDifferent(Color color1, Color color2) {
        boolean different = false;
        int redDiff = Math.abs(color1.getRed() - color2.getRed());
        int greenDiff = Math.abs(color1.getGreen() - color2.getGreen());
        int blueDiff = Math.abs(color1.getBlue() - color2.getBlue());
        if (redDiff + greenDiff + blueDiff > 55) {
            different = true;
        }
        return different;
    }
}
