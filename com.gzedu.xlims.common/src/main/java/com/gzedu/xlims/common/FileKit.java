package com.gzedu.xlims.common;

import java.io.File;

/**
 * 文件操作类
 *
 * Created by paul on 2017/4/10.
 */
public class FileKit {

    private static final long M = 1048576;

    // 在文件夹内 根据文件大小 判断是否删除

    public static  boolean delFile(String target){
        File file =  new File(target);
        return deleteDir(file);
    }

    /**
     *
     * @param dir       文件(绝对路径)
     * @param maxSize   文件大小 M
     * 超过 限制文件大小的文件删除  返回删除的文件数
     */
    public static int delFileByFileSize(String dir,int maxSize){
        int result =0;
        File target = new File(dir);
        long size = M* maxSize;
        if (target.isDirectory()){
            File[] files = target.listFiles();
            for(File f:files){
                if (f.length()>size){
                    if(f.delete())
                        result++;
                }
            }
        }else if(target.length()>size){
            if(target.delete())
                result++;
        }
        return result;
    }

    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     *测试
     */
    public static void main(String[] args) {
        doDeleteEmptyDir("new_dir1");
        String newDir2 = "new_dir2";
        boolean success = deleteDir(new File(newDir2));
        if (success) {
            System.out.println("Successfully deleted populated directory: " + newDir2);
        } else {
            System.out.println("Failed to delete populated directory: " + newDir2);
        }
    }

}
