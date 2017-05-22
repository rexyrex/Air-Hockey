package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;


public class Main extends SimpleApplication {
    
Cylinder handle1;
Cylinder handle2;
Cylinder puck;
Geometry handleGeom1;
Geometry handleGeom2;
Geometry puckGeom;
Geometry tableTopGeom;
Geometry tableSideGeom1;
Geometry tableSideGeom2;
Geometry tableFrontGeom1;
Geometry tableFrontGeom2;
Geometry tableBackGeom1;
Geometry tableBackGeom2;

Material mat;
Material tableTopMat;
Material tableSideMat;
Material tableFrontAndBackMat;
Material handle1Mat;
Material puckMat;

AudioNode bMusic;
AudioNode booSound;
AudioNode cheerSound;

private float aiIniSpeed = 4;
private String[] aiLevels = {"Noob", "Bad", "Normal", "Good", "Hard", "Expert", "Impossibru"};

private static final float distFromTable = 2.2f;
private static final int scale = 24;

private int playerScore=0;
private int aiScore=0;

BitmapText playerScoreDisplay;
BitmapText aiScoreDisplay;
BitmapText aiLevelDisplay;

private RigidBodyControl table_phy;
private RigidBodyControl handle_phy;
private RigidBodyControl handle2_phy;
private RigidBodyControl puck_phy;
Menu startMenu = new Menu();

private BulletAppState bulletAppState;
    public static void main(String[] args) {
        
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // flyCam.setEnabled(false);
        
        setDisplayFps(false);
        setDisplayStatView(false);      
        
        stateManager.attach(startMenu);
       // this.paused = true;
        
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        initMusic();
        initMaterials();
        initTable();
        initHandles();
        initPuck();
        initScoreDisplay();
        initNodes();
        
        inputManager.addMapping("AiLevelIncrease", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("AiLevelDecrease", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(actionListener, "AiLevelIncrease", "AiLevelDecrease");
        
        cam.setLocation(new Vector3f(0, 7*scale, 27*scale));        
        //cam.lookAtDirection(new Vector3f(10*scale, 1*scale, 15*scale), new Vector3f(10*scale, 1*scale, 15*scale));
    }
    
    private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("AiLevelIncrease") && !keyPressed) {
        if(aiIniSpeed<8)
          aiIniSpeed+=1;
      }
      if (name.equals("AiLevelDecrease") && !keyPressed) {
          if(aiIniSpeed>2)
             aiIniSpeed-=1;
      } 
    }
  };

    
    public void initScoreDisplay(){
        
        playerScoreDisplay = new BitmapText(guiFont, false);
        aiScoreDisplay = new BitmapText(guiFont, false);
        aiLevelDisplay = new BitmapText(guiFont, false);
        
        playerScoreDisplay.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        playerScoreDisplay.setColor(ColorRGBA.White);                             // font color
        playerScoreDisplay.setText("Player Score : 0");             // the text
        playerScoreDisplay.setLocalTranslation(9*scale, 8*scale, -4*scale); // position
        
        aiScoreDisplay.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        aiScoreDisplay.setColor(ColorRGBA.White);                             // font color
        aiScoreDisplay.setText("Computer Score: 0");             // the text
        aiScoreDisplay.setLocalTranslation(-9*scale, 8*scale, -4*scale); // position
        
        aiLevelDisplay.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        aiLevelDisplay.setColor(ColorRGBA.White);                             // font color
        aiLevelDisplay.setText("AI Difficulty :");             // the text
        aiLevelDisplay.setLocalTranslation(0, 8*scale, -4*scale); // position
        
        
        
    }
    
    public void initTable(){        
        Box tableTop = new Box(10*scale, 1*scale, 15*scale);
        Box tableSide1 = new Box(1*scale, 1*scale, 15*scale);
        Box tableSide2 = new Box(1*scale, 1*scale, 15*scale);
        Box tableFront1 = new Box(4*scale, 1*scale, 1*scale);
        Box tableFront2 = new Box(4*scale, 1*scale, 1*scale);
        Box tableBack1 = new Box(4*scale, 1*scale, 1*scale);
        Box tableBack2 = new Box(4*scale, 1*scale, 1*scale);
        
        tableTopGeom = new Geometry("Table Top", tableTop);
        tableSideGeom1 = new Geometry("TableRightSide", tableSide1);
        tableSideGeom2 = new Geometry("TableLeftSide", tableSide2);
        tableFrontGeom1 = new Geometry("Table Front Right", tableFront1);
        tableFrontGeom2 = new Geometry("Table Front Right", tableFront2);
        tableBackGeom1 = new Geometry("Table Front Right", tableBack1);
        tableBackGeom2 = new Geometry("Table Front Right", tableBack2);
        
        tableTopGeom.setMaterial(mat);
        tableSideGeom1.setMaterial(tableSideMat);
        tableSideGeom2.setMaterial(tableSideMat);
        tableFrontGeom1.setMaterial(tableFrontAndBackMat);
        tableFrontGeom2.setMaterial(tableFrontAndBackMat);
        tableBackGeom1.setMaterial(tableFrontAndBackMat);
        tableBackGeom2.setMaterial(tableFrontAndBackMat);
        
        tableSideGeom1.setLocalTranslation(9f*scale, 1*scale, 0*scale);
        tableSideGeom2.setLocalTranslation(-9f*scale, 1*scale, 0*scale);
        tableFrontGeom1.setLocalTranslation(5.5f*scale, 1*scale, 14*scale);
        tableFrontGeom2.setLocalTranslation(-5.5f*scale, 1*scale, 14*scale);
        tableBackGeom1.setLocalTranslation(5.5f*scale, 1*scale, -14*scale);
        tableBackGeom2.setLocalTranslation(-5.5f*scale, 1*scale, -14*scale);
        
        table_phy = new RigidBodyControl(0.0f);
        
        RigidBodyControl tableTop_phy = new RigidBodyControl(0.0f);
        RigidBodyControl tableSide1_phy = new RigidBodyControl(0.0f);
        RigidBodyControl tableSide2_phy = new RigidBodyControl(0.0f);
        RigidBodyControl tableFront1_phy = new RigidBodyControl(0.0f);
        RigidBodyControl tableFront2_phy = new RigidBodyControl(0.0f);
        RigidBodyControl tableBack1_phy = new RigidBodyControl(0.0f);
        RigidBodyControl tableBack2_phy = new RigidBodyControl(0.0f);
        
        RigidBodyControl table_bodies[] = {tableSide1_phy,tableSide2_phy, tableFront1_phy, tableFront2_phy,
        tableBack1_phy, tableBack2_phy};
        
        tableTopGeom.addControl(tableTop_phy);
        tableSideGeom1.addControl(tableSide1_phy);
        tableSideGeom2.addControl(tableSide2_phy);
        tableFrontGeom1.addControl(tableFront1_phy);
        tableFrontGeom2.addControl(tableFront2_phy);
        tableBackGeom1.addControl(tableBack1_phy);
        tableBackGeom2.addControl(tableBack2_phy);
        
        tableTop_phy.setFriction(0.0f);
        
        for(RigidBodyControl rbc : table_bodies){
            rbc.setRestitution(1f);
            rbc.setFriction(0.1f);
        }       
        
        bulletAppState.getPhysicsSpace().add(tableTopGeom);
        bulletAppState.getPhysicsSpace().add(tableSideGeom1);
        bulletAppState.getPhysicsSpace().add(tableSideGeom2);
        bulletAppState.getPhysicsSpace().add(tableFrontGeom1);
        bulletAppState.getPhysicsSpace().add(tableFrontGeom2);
        bulletAppState.getPhysicsSpace().add(tableBackGeom1);
        bulletAppState.getPhysicsSpace().add(tableBackGeom2);
        
        
    }
    
    public void initNodes(){
        Node table = new Node("Table");
        Node tableSide = new Node("Table Side");
        Node tableFront = new Node("Table Front");
        Node tableBack = new Node("Table Back");
        Node handleNode = new Node("Handle");
        
        handleNode.attachChild(handleGeom1);
        handleNode.attachChild(handleGeom2);

        tableSide.attachChild(tableSideGeom1);
        tableSide.attachChild(tableSideGeom2);
        tableFront.attachChild(tableFrontGeom1);
        tableFront.attachChild(tableFrontGeom2);
        tableBack.attachChild(tableBackGeom1);
        tableBack.attachChild(tableBackGeom2);
        
        table.attachChild(tableSide);
        table.attachChild(tableTopGeom);
        table.attachChild(tableFront);
        table.attachChild(tableBack);
        
        
        rootNode.attachChild(playerScoreDisplay);
        rootNode.attachChild(aiScoreDisplay);
        rootNode.attachChild(aiLevelDisplay);
        rootNode.attachChild(table);
        rootNode.attachChild(handleNode);
        rootNode.attachChild(puckGeom);
    }
    
    public void initMaterials(){
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        tableSideMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        tableFrontAndBackMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        handle1Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        puckMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        Texture tableTopTexture = assetManager.loadTexture("Materials/tableTopTex.jpg");
        Texture tableSideTexture = assetManager.loadTexture("Materials/sideTex.jpg");
        //Texture puckTexture = assetManager.loadTexture("Materials/puckTex.jpg");
        Texture handleTexture = assetManager.loadTexture("Materials/handleTex.jpg");
        
        //puckMat.setTexture("ColorMap", puckTexture);
        mat.setTexture("ColorMap", tableTopTexture);
        tableSideMat.setTexture("ColorMap", tableSideTexture); 
        tableFrontAndBackMat.setTexture("ColorMap", tableSideTexture);
        handle1Mat.setTexture("ColorMap", handleTexture);
        
        mat.setColor("Color", ColorRGBA.White);
        tableSideMat.setColor("Color", ColorRGBA.Blue);
        tableFrontAndBackMat.setColor("Color", ColorRGBA.Cyan);
        //handle1Mat.setColor("Color", ColorRGBA.Yellow);
        puckMat.setColor("Color", ColorRGBA.Red);
    }
    
    public void initMusic(){
        bMusic = new AudioNode(assetManager, "Sounds/PlayWithMeMono.wav", true);
        bMusic.play();
        booSound = new AudioNode(assetManager, "Sounds/boo.ogg");
        cheerSound = new AudioNode(assetManager, "Sounds/cheerMono.ogg");
    }

    public void initHandles(){
        handle1 = //new Sphere(200,200,1.0f*scale);
                new Cylinder(100, 100, 1.0f*scale, 1.0f*scale,true );
        
        handle2 = //new Sphere(200,200,1.0f*scale);
                 new Cylinder(100, 100, 1.0f*scale, 1.0f*scale,true );
        
        handleGeom1 = new Geometry("Player Handle", handle1);        
        handleGeom1.rotate(90*FastMath.DEG_TO_RAD, 0, 0);        
        handleGeom2 = new Geometry("Computer Handle", handle2); 
        
        handleGeom1.setMaterial(handle1Mat);
        handleGeom2.setMaterial(handle1Mat);
        
        handleGeom1.setLocalTranslation(0, 3f*scale, 7f*scale);
        handleGeom2.setLocalTranslation(0, 3f*scale, -7f*scale);        
        
        handle_phy = new RigidBodyControl(2.0f);
        handleGeom1.addControl(handle_phy);
        bulletAppState.getPhysicsSpace().add(handle_phy);
        handle_phy.setRestitution(1.0f);
        
        handle2_phy = new RigidBodyControl(2.0f);
        handleGeom2.addControl(handle2_phy);
        bulletAppState.getPhysicsSpace().add(handle2_phy);
        handle2_phy.setRestitution(1.0f);    
    }
    
    public void initPuck(){
        
        puck = //new Sphere(200,200,1.0f*scale);
                new Cylinder(100, 100, 1.0f*scale, 1.0f*scale,true );
        
        puckGeom = new Geometry("The Puck", puck);        
        puckGeom.rotate(90*FastMath.DEG_TO_RAD, 0, 0);
        
        puckGeom.setLocalTranslation(0,3f*scale,0);        
        
        puck_phy = new RigidBodyControl(2.0f);
        puckGeom.addControl(puck_phy);
        bulletAppState.getPhysicsSpace().add(puck_phy);
        puck_phy.setRestitution(1.0f);
        puck_phy.setFriction(0.1f);
        
        puckGeom.setMaterial(puckMat); 
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        float x, y, puckX, puckZ, puckY, aiX, aiZ;
        float playerGoalX, playerGoalZ, aiGoalX, aiGoalZ;
       
        playerGoalX = 0;
        playerGoalZ = 15f * scale;
        aiGoalX = 0;
        aiGoalZ = -15f * scale;
        
        x = inputManager.getCursorPosition().getX();
        y = inputManager.getCursorPosition().getY();
        
        if(y>0){
            y=0;
        }
        if(y < -13f*scale){
            y = -13f*scale;
        }
        
        if(x>8f * scale){
            x = 8f * scale;
        }
        if(x<-8f * scale){
            x = -8f * scale;
        }
        
        //handle_phy.setPhysicsLocation(x, 1*scale, -y);
        handle_phy.setPhysicsLocation(new Vector3f(x, distFromTable*scale, -y));
      
      //  handle_phy.setAngularVelocity(new Vector3f(0,0,0));        
       // handle2_phy.setAngularVelocity(new Vector3f(0,0,0));
        //puck_phy.setAngularVelocity(new Vector3f(0,0,0));
         /* 
        Matrix3f mat = new Matrix3f();
        mat.setColumn(0, new Vector3f(0,0,0));
        mat.setColumn(1, new Vector3f(0,0,0));
        mat.setColumn(2, new Vector3f(0,0,0));

        handle_phy.setPhysicsRotation(mat);
        handle2_phy.setPhysicsRotation(mat);
        puck_phy.setPhysicsRotation(mat);
        */
        puckX = puck_phy.getPhysicsLocation().getX();
        puckZ = puck_phy.getPhysicsLocation().getZ();
        puckY = puck_phy.getPhysicsLocation().getY();
        Vector3f puckVec = puck_phy.getLinearVelocity();
        
        puck_phy.setPhysicsLocation(new Vector3f(puck_phy.getPhysicsLocation().getX(), distFromTable*scale, puck_phy.getPhysicsLocation().getZ()));
        
        if(puckZ<-13f*scale){
            puck_phy.setPhysicsLocation(new Vector3f(puckX,distFromTable*scale,-13f*scale));
            puck_phy.setLinearVelocity(new Vector3f(puckVec.getX(), puckVec.getY(), -puckVec.getZ()));
        }
        if(puckZ > 13f*scale){            
            puck_phy.setPhysicsLocation(new Vector3f(puckX,distFromTable*scale,13f*scale));
            puck_phy.setLinearVelocity(new Vector3f(puckVec.getX(), puckVec.getY(), -puckVec.getZ()));
        }
        
        if(puckX>8f * scale){
             puck_phy.setPhysicsLocation(new Vector3f(8f * scale,distFromTable*scale,puckZ));
             puck_phy.setLinearVelocity(new Vector3f(-puckVec.getX(), puckVec.getY(), puckVec.getZ()));
        }
        if(puckX<-8f * scale){
            puck_phy.setPhysicsLocation(new Vector3f(-8f * scale,distFromTable*scale,puckZ));
            puck_phy.setLinearVelocity(new Vector3f(-puckVec.getX(), puckVec.getY(), puckVec.getZ()));
        }
        
        
        //Ai movements
        aiX = handle2_phy.getPhysicsLocation().getX();
        aiZ = handle2_phy.getPhysicsLocation().getZ();
        
        
        handle2_phy.setPhysicsLocation(new Vector3f(aiX, distFromTable*scale, aiZ));
        float aiDestX, aiDestZ;
        //evaluate distance between AI handle and goal
        double puckGoaldistance;
        puckGoaldistance = Math.sqrt((puckX-aiGoalX)*(puckX-aiGoalX)+(puckZ-aiGoalZ)*(puckZ-aiGoalZ));
         System.out.println("dist:"+puckGoaldistance);
         System.out.println("check dist:"+6*scale);
         
         float aiSpeed = aiIniSpeed;
         
        //if(puckGoaldistance < 7*scale){
         if(puckZ <-2f*scale && puckZ >-8f*scale){
            aiDestX = puckX;
            aiDestZ = puckZ;
            aiSpeed *= 1.5;
           // System.out.println("dist lower");
        } else if(puckZ < -8f*scale){
            if((int)Math.random()*2>1){
                aiDestX = puckX;//(puckZ*-7f*scale)/puckX;
                aiDestZ = -8f*scale;
                aiSpeed *= 1.5;
            } else {
                aiDestX = 0f;
                aiDestZ = -9f * scale;
            }
        }else {
            //aiDestX = 0f;
            //aiDestZ = -7f * scale;
             //System.out.println("else");
             aiDestX = puckX;//(puckZ*-7f*scale)/puckX;
             aiDestZ = -8f*scale;
        }
        
        handle2_phy.setLinearVelocity(new Vector3f((aiDestX-aiX)*aiSpeed, distFromTable*scale, (aiDestZ-aiZ)*aiSpeed));
        
        
        if(puckZ<-12f*scale && puckX<2f*scale && puckX>-2f){
            playerScore++;
            puck_phy.setPhysicsLocation(new Vector3f(0,distFromTable*scale,0));
            cheerSound.play();
        }
        
        if(puckZ>12f*scale && puckX<2f*scale && puckX>-2f){
            aiScore++;
            puck_phy.setPhysicsLocation(new Vector3f(0,distFromTable*scale,0));
            booSound.play();
        }
        
        aiScoreDisplay.setText("Player Score: "+playerScore);  
        playerScoreDisplay.setText("Computer Score: "+ aiScore);  
        aiLevelDisplay.setText("AI Difficulty : "+aiLevels[(int)aiIniSpeed-2]);
        
        if(playerScore >=5){
            stateManager.detach(bulletAppState);
            cheerSound.play();
            playerScoreDisplay.setText("You win!");  
            aiScoreDisplay.setText("Congradulations!"); 
            
        }
        
        if(aiScore >=5){
            stateManager.detach(bulletAppState);
            booSound.play();
            playerScoreDisplay.setText("You lose!");  
            aiScoreDisplay.setText("Go practice!");             
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
