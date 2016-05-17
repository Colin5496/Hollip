#include <EEPROM.h>
#include <Adafruit_NeoPixel.h>
#include <SoftwareSerial.h>
#define PIN            6

  Adafruit_NeoPixel pixels = Adafruit_NeoPixel(1, PIN, NEO_GRB + NEO_KHZ800);
  SoftwareSerial BTSerial(8, 9); // SoftwareSerial(TX, RX)  
  int sw1;
  int sw2;
  int sw3;
  char data;
  String myString;
  int rgb[3];
  int index=0;
  byte r = 0, g = 0, b = 0;
  int saver = 1;
  int saveg = 2;
  int saveb = 3;
  boolean islighton = false;

void setup() {
  pixels.begin();
  BTSerial.begin(9600); 
  Serial.begin(9600); 
  pinMode(10, INPUT_PULLUP);
  pinMode(11, INPUT_PULLUP);
  pinMode(12, INPUT_PULLUP);
}

void loop() {
  sw1 = digitalRead(10);
  sw2 = digitalRead(11);
  sw3 = digitalRead(12);


    if(sw1 == 0)
    {
      if(!islighton)
      {
        r = EEPROM.read(saver);
        g = EEPROM.read(saveg);
        b = EEPROM.read(saveb);
        pixels.setPixelColor(0, pixels.Color(r, g, b));
        pixels.show();
        islighton = true;
        delay(1000);
      }
      else if(islighton)
      {
        r = 0;
        g = 0;
        b = 0;
        pixels.setPixelColor(0, pixels.Color(r, g, b));
        pixels.show();
        islighton = false;
        delay(1000);
      }
      // 스마트폰으로 문자열 전송
      /*BTSerial.println("one");     
      delay(1500);*/ 
    }
    if(sw2 == 0)
    {
      // 스마트폰으로 문자열 전송
      BTSerial.println("two");     
      delay(1500); 
    }
    if(sw3 == 0)
    {
      // 스마트폰으로 문자열 전송
      BTSerial.println("three");     
      delay(1500); 
    }
    
    while(BTSerial.available())
    {
        char data = (char)BTSerial.read();
        myString += data;
        delay(5);        
    }
    
    if(myString.equals("dial\n")){
        pixels.setPixelColor(0, pixels.Color(0,255,50));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,255,50));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,255,50));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(1000);
        myString="";
        pixels.setPixelColor(0, pixels.Color(r, g, b));
        pixels.show();
    }
    else if(myString.equals("sms\n")){
        pixels.setPixelColor(0, pixels.Color(0,45,255));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,45,255));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,45,255));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(1000);
        myString="";
        pixels.setPixelColor(0, pixels.Color(r, g, b));
        pixels.show();
    }
    else if(myString.equals("kakao\n")){
        pixels.setPixelColor(0, pixels.Color(255,233,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(255,233,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(255,233,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(1000);
        myString="";
        pixels.setPixelColor(0, pixels.Color(r, g, b));
        pixels.show();
    }
    else if(myString.equals("custom\n")){
        pixels.setPixelColor(0, pixels.Color(0,255,184));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,255,184));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,255,184));
        pixels.show();
        delay(400);
        pixels.setPixelColor(0, pixels.Color(0,0,0));
        pixels.show();
        delay(1000);
        myString="";
        pixels.setPixelColor(0, pixels.Color(r, g, b));
        pixels.show();
    }
    else if(!myString.equals("") && !myString.equals("dial") && !myString.equals("sms") && !myString.equals("kakao") && !myString.equals("custom")){
      Split(myString,',');
      myString="";
    }
    /*if(!myString.equals(""))
    {
        Serial.println(myString);
        if(myString.equals("sms\n"))
        {
          Serial.println(myString+"zz");
          pixels.setPixelColor(0, pixels.Color(0,255,184));
          pixels.show();
          delay(400);
          pixels.setPixelColor(0, pixels.Color(0,0,0));
          pixels.show();
          delay(400);
          pixels.setPixelColor(0, pixels.Color(0,255,184));
          pixels.show();
          delay(400);
          pixels.setPixelColor(0, pixels.Color(0,0,0));
          pixels.show();
          delay(1000);
          myString="";
          pixels.setPixelColor(0, pixels.Color(r, g, b));
          pixels.show();        
        }
    }*/
    
}

void Split(String sData, char cSeparator)
{   
    int nCount = 0;
    int nGetIndex = 0 ;
 
    //임시저장
    String sTemp = "";
 
    //원본 복사
    String sCopy = sData;
 
    while(true)
    {
        //구분자 찾기
        nGetIndex = sCopy.indexOf(cSeparator);
 
        //리턴된 인덱스가 있나?
        if(-1 != nGetIndex)
        {
            //있다.
 
            //데이터 넣고
            sTemp = sCopy.substring(0, nGetIndex);
            rgb[index++]=atoi(sTemp.c_str());
            //Serial.println( sTemp );
         
            //뺀 데이터 만큼 잘라낸다.
            sCopy = sCopy.substring(nGetIndex + 1);
        }
        else
        {
            //없으면 마무리 한다.
            //Serial.println( sCopy );
            rgb[index]=atoi(sCopy.c_str());
            index=0;
            break;
        }
 
        //다음 문자로~
        ++nCount;
    }
    //Serial.println(rgb[0]);
    //Serial.println(rgb[1]);
    //Serial.println(rgb[2]);
    
    r = rgb[0];
    g = rgb[1];
    b = rgb[2];
    
    //내부롬 저장
    if(r==0 && g==0 && b==0)
    {
      islighton = false;
      pixels.setPixelColor(0, pixels.Color(r,g,b));
      pixels.show();      
    }
    else
    {
      EEPROM.write(saver, r);
      EEPROM.write(saveg, g);
      EEPROM.write(saveb, b);
      islighton = true;
      pixels.setPixelColor(0, pixels.Color(r,g,b));
      pixels.show();
    }
    
}
