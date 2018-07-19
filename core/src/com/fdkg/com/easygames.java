package com.fdkg.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import javax.xml.soap.Text;

public class easygames extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] dino;


	int dinoState = 0;
	int pause = 0;
	float gravity = 0.5f;
	float velocity = 0;
	int dinoY = 0;
	int dinoX = 0;

	Rectangle dinoRectangle;
	BitmapFont font;
	Texture bunny;

	int score = 0;
	int gameState = 0;

	Music sound;
	Music sound2;
	Music sound3;

	Random random;

	ArrayList<Integer> meatsX = new ArrayList<Integer>();
	ArrayList<Integer> meatsY = new ArrayList<Integer>();
	ArrayList<Rectangle> meatRectangles = new ArrayList<Rectangle>();
	Texture meat;
	int meatCount;

	ArrayList<Integer> carrotsX = new ArrayList<Integer>();
	ArrayList<Integer> carrotsY = new ArrayList<Integer>();
	ArrayList<Rectangle> carrotRectangles = new ArrayList<Rectangle>();

	Texture carrot;
	int carrotCount;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("hatter2.jpg");

		dino = new Texture[4];
		dino[0] = new Texture("dino1jo.png");
		dino[1] = new Texture("dino02.png");
		dino[2] = new Texture("dino3.png");
		dino[3] = new Texture("dino4.png");

		dinoY = Gdx.graphics.getHeight() /2;

		sound = Gdx.audio.newMusic(Gdx.files.internal("pop.ogg"));
		sound2 = Gdx.audio.newMusic(Gdx.files.internal("running.mp3"));
		sound3 = Gdx.audio.newMusic(Gdx.files.internal("steak.mp3"));



		meat = new Texture("steak.png");
		carrot = new Texture("carrot.png");
		random = new Random();

		bunny = new Texture("bunny.png");

		font = new BitmapFont();
		font.setColor(Color.GREEN);
		font.getData().setScale(8);


	}

	public void makeMeat() {
		float height = random.nextFloat() * Gdx.graphics.getHeight() ;
		meatsY.add((int) height);
		meatsX.add( Gdx.graphics.getWidth());
	}

	public void makeCarrot() {
		float height = random.nextFloat() * Gdx.graphics.getHeight() ;
		carrotsY.add((int) height);
		carrotsX.add( Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if(gameState == 1) {
			//game is live
			//Repa
			if(carrotCount < 250 ) {
				carrotCount++;
			} else {
				carrotCount = 0;
				makeCarrot();
			}



			carrotRectangles.clear();
			for(int i = 0; i < carrotsX.size(); i++){
				batch.draw(carrot, carrotsX.get(i), carrotsY.get(i));
				carrotsX.set(i, carrotsX.get(i) -12);
				carrotRectangles.add(new Rectangle(carrotsX.get(i),carrotsY.get(i), carrot.getWidth(), carrot.getHeight()));

			}

			//Steak
			if(meatCount < 100 ) {
				meatCount++;
			} else {
				meatCount = 0;
				makeMeat();
			}


			meatRectangles.clear();
			for(int i = 0; i < meatsX.size(); i++){
				batch.draw(meat, meatsX.get(i), meatsY.get(i));
				meatsX.set(i, meatsX.get(i) -10);
				meatRectangles.add(new Rectangle(meatsX.get(i),meatsY.get(i), meat.getWidth(), meat.getHeight()));
			}

			if(Gdx.input.justTouched()){ //Jumping
				velocity = -9;

		}

			if(pause < 12) {
				pause++;
			}else {
				pause = 0;
				if (dinoState < 3) {
					dinoState++;
				} else {
					dinoState = 0;
				}
			}


			velocity += gravity;
			dinoY -= velocity;


			if(dinoY <=0){ //nem esik ki a kepbol
				dinoY = 0;
			}
			if(dinoY == 0 ) {
				sound2.play();
			} else if ( gameState == 0 || gameState == 2){
				sound2.stop();
			} else {
				sound2.stop();
			}

		}else if (gameState == 0) {
			//waiting to start
			sound2.stop();
			if(Gdx.input.justTouched()) {
				gameState = 1;

			}
		}else if (gameState == 2) {
			//Game over
			if(Gdx.input.justTouched()) {
				gameState = 1;
				dinoY = Gdx.graphics.getHeight() /2;
				score = 0;
				velocity = 0;
				meatsX.clear();
				meatsY.clear();
				meatRectangles.clear();
				meatCount = 0;
				sound2.stop();

				carrotsX.clear();
				carrotsY.clear();
				carrotRectangles.clear();
				carrotCount = 0;
			}
		}

		if (gameState == 2) {
			batch.draw(bunny,Gdx.graphics.getWidth() / 2 - dino[dinoState].getWidth() / 2 - 300, dinoY );

		} else {
			batch.draw(dino[dinoState], Gdx.graphics.getWidth() / 2 - dino[dinoState].getWidth() / 2 - 300, dinoY);

		}
		dinoRectangle = new Rectangle(Gdx.graphics.getWidth() /2 - dino[dinoState].getWidth() /2 -300, dinoY, dino[dinoState].getWidth(), dino[dinoState].getHeight());

		for(int i = 0; i <meatRectangles.size(); i++) {
			if(Intersector.overlaps(dinoRectangle,meatRectangles.get(i))) {
					//Gdx.app.log("HUS!","Steak!");
					score++;
					sound.play();
					meatRectangles.remove(i);
					meatsX.remove(i);
					meatsY.remove(i);
					break;
			}
		}

		for(int i = 0; i <carrotRectangles.size(); i++) {
			if(Intersector.overlaps(dinoRectangle,carrotRectangles.get(i))) {
				Gdx.app.log("Repa!","Zoldseg!");
				gameState = 2;
			}
		}

		font.draw(batch, String.valueOf(score), 30,740);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sound.dispose();
		sound2.dispose();
		sound3.dispose();
	}
}
