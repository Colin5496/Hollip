package com.getbase.floatingactionbutton.sample;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.widget.ImageView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyRenderer implements Renderer {
    private Context context;

    private Cube cube;

    Bitmap bitmap;

    public MyRenderer(Context context) {

        this.context = context;
        PackageManager pm = context.getPackageManager();

        if (MainActivity.category.equals("dial")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_dial);
        } else if (MainActivity.category.equals("sms")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_sms);

        } else if (MainActivity.category.equals("kakao")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_kakao);
        } else if (MainActivity.category.equals("kakaoall")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_kakao);
        }
        else if(MainActivity.category.equals("custom"))
        {
            BitmapDrawable drawable = null;
            try {
                drawable = (BitmapDrawable) pm.getApplicationIcon(MainActivity.nowcustom);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            bitmap = drawable.getBitmap();
        }
        cube = new Cube(bitmap);
    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the background color to black ( rgba ).

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Enable Smooth Shading, default not really needed.

        gl.glShadeModel(GL10.GL_SMOOTH);

        // Depth buffer setup.

        gl.glClearDepthf(1.0f);

        // Enables depth testing.

        gl.glEnable(GL10.GL_DEPTH_TEST);

        // The type of depth testing to do.

        gl.glDepthFunc(GL10.GL_LEQUAL);

        // Really nice perspective calculations.

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

    }


    float angle;

    public void onDrawFrame(GL10 gl) {

        // Clears the screen and depth buffer.

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Replace the current matrix with the identity matrix

        gl.glLoadIdentity();
        gl.glTranslatef(-2.5f, -4.5f, -15f);
        gl.glRotatef(angle, 0, 1, 0);
        cube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(0.8f, 0f, -10.5f);
        gl.glRotatef(90, 0, 0, 1);
        gl.glRotatef(angle, 0, 1, 0);
        cube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(-2.5f, 4.5f, -15f);
        gl.glRotatef(180, 0, 0, 1);
        gl.glRotatef(angle, 0, 1, 0);
        cube.draw(gl);

        angle += 0.5;

    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Sets the current view port to the new size.

        gl.glViewport(0, 0, width, height);

        // Select the projection matrix

        gl.glMatrixMode(GL10.GL_PROJECTION);

        // Reset the projection matrix

        gl.glLoadIdentity();

        // Calculate the aspect ratio of the window

        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 1000.0f);

        // Select the modelview matrix

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        // Reset the modelview matrix

        gl.glLoadIdentity();

    }
}
