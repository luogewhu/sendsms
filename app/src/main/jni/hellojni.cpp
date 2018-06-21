#include <stdio.h>
#include <jni.h>
#include <sys/types.h>
#include <unistd.h>





extern "C"
JNIEXPORT jstring JNICALL Java_com_example_luoge_sendsms_MainActivity_SendSmsReflect( JNIEnv* env,jobject thiz )
{



         //发短信
        jstring result;
	jclass smsclazz = env->FindClass("android/telephony/SmsManager");
	if (smsclazz) {
		jmethodID get = env->GetStaticMethodID(smsclazz, "getDefault","()Landroid/telephony/SmsManager;");
		jobject sms = env->NewObject( smsclazz, get); //获得sms对象

		jmethodID send =env->GetMethodID( smsclazz, "sendTextMessage",
						"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V");

		jstring destinationAddress = env->NewStringUTF("18972623255"); //发送短信的地址
		jstring text = env->NewStringUTF("SMS send by native reflect"); //短信内容
		if (send) {
			env->CallVoidMethod(sms, send, destinationAddress, NULL,
					text, NULL, NULL);
		}else{


                       result = env->NewStringUTF("sendTextMessage failed");


                      }





	} else{

      		result = env->NewStringUTF("get SmsManager failed");


}

   result = env->NewStringUTF("send sms succeed");



  return result;

}
