package org.beacon.client;

import java.io.*;
import java.net.SocketException;
import java.util.*;

public class App {
    public static void main(String[] args) {
        List<String> fullFileNames =null;
        String username;
        username = args[0];
        System.out.println(username);
        final Properties properties = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream("client-config.properties");
            fullFileNames = getFileList(classLoader);
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            List<String> fileList = constructRandomFileList(fullFileNames);
            System.out.println("Randomly choosen files are ....");
            for (int j=0;j<fileList.size();j++) {
                System.out.println(fileList.get(j));
            }
            Node node = new Node(username,properties,fileList);
            Thread.sleep(10);
            Thread worker = new Worker(node);
            worker.start();
            node.register();

            //check whether node is actually registered
            int count =0;
            while (true) {
                Thread.sleep(500);
                if (node.isRegistered()) {
                    System.out.println("Node registered");
                    break;
                } else {
                    if(count>4) {
                        System.out.println("can not register.. quitting");
                        System.exit(1);
                    }
                    count++;
                    continue;
                }
            }

            node.join();
            Thread.sleep(2000);
            System.out.println("Enter input");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.next();
                if (input.equals("exit")) {
                    System.out.println(input);

                    break;
                } else {
                    System.out.println(input + "else");
                }

            }
            worker.join();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getFileList(ClassLoader classLoader) {
        List<String> filesNames = new ArrayList<String>();
        try {
            InputStream stream = classLoader.getResourceAsStream("fileNames.txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
            String line = buf.readLine();
            while(line != null){
                filesNames.add(line);
                line = buf.readLine();;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filesNames;


    }


    private static List<String> constructRandomFileList(List<String> fullFileList) {
        if (fullFileList == null) {
            System.out.println("full file list is empty");
            return null;
        }
        List<String> fileList = new ArrayList<String>();
        int numOfFiles = generateRandomNumber(3,5);
        for (int i=0;i<numOfFiles;i++) {
            int rn = generateRandomNumber(0,20);
            fileList.add(fullFileList.get(rn));
        }

        return fileList;
    }

    private static int generateRandomNumber(int min,int max) {
        int response;
        //Date date = new Date();
        Random random = new Random();
        response = min + random.nextInt(max-min);;

        return response;
    }
}
