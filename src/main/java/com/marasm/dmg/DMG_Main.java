/**
 * Created by Sergey Rump on 19.08.2016.
 */
package com.marasm.dmg;
public class DMG_Main
{
    public static void main(String[] args)
    {
        for (String str : args)
        {
            DMGParser parser = new DMGParser(str);
            for (DMGObject obj : parser.objects)
            {
                System.out.println("\n"+obj.toString(2));
            }
        }
    }
}
