/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <jni.h>
#include "SerialPort.h"

#include "android/log.h"
static const char *TAG="serial_port";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define DEFAULT_SERIAL_NODE "/sys/bus/platform/devices/urovo-scan.0/open"
#define DEFAULT_SERIAL_SPEED 115200

int fd_serial;

static speed_t getBaudrate(jint baudrate)
{
	switch(baudrate) {
	case 0: return B0;
	case 50: return B50;
	case 75: return B75;
	case 110: return B110;
	case 134: return B134;
	case 150: return B150;
	case 200: return B200;
	case 300: return B300;
	case 600: return B600;
	case 1200: return B1200;
	case 1800: return B1800;
	case 2400: return B2400;
	case 4800: return B4800;
	case 9600: return B9600;
	case 19200: return B19200;
	case 38400: return B38400;
	case 57600: return B57600;
	case 115200: return B115200;
	case 230400: return B230400;
	case 460800: return B460800;
	case 500000: return B500000;
	case 576000: return B576000;
	case 921600: return B921600;
	case 1000000: return B1000000;
	case 1152000: return B1152000;
	case 1500000: return B1500000;
	case 2000000: return B2000000;
	case 2500000: return B2500000;
	case 3000000: return B3000000;
	case 3500000: return B3500000;
	case 4000000: return B4000000;
	default: return -1;
	}
}

/*
 *	switch the serial node
 *	write serial port
 */
int maxqpro_pinctl(char onoff, const char* name) {
	int ret = 0;
	int fd_sys = -1;
	char buff[1];
	fd_sys = open(name, O_RDWR | O_SYNC);
	if (fd_sys <= 0) {
		//printf("change pin error %d\n", errno);
		LOGE("change pin error\n");
		return -1;
	}
	buff[0] = onoff;
	LOGD("onoff = %d\n", onoff);
	ret = write(fd_sys, buff, 1);
	if (ret < 0) {
		return -1;
	}
	usleep(1000 * 1000);

	return 1;
}

/*
 *
 *	write serial port
 */
int maxqpro_pinctl_fd(int fd, char onoff) {
	int ret = 0;
	int fd_sys = -1;
	char buff[1];
	fd_sys = fd;
	if (fd_sys <= 0) {
		//printf("change pin error %d\n", errno);
		LOGE("change pin error\n");
		return -1;
	}
	buff[0] = onoff;
	ret = write(fd_sys, buff, 1);
	if (ret < 0) {
		return -1;
	}
	usleep(1000 * 1000);

	return 1;
}

/**
 *@brief  设置串口通信速率
 *@param  fd     类型 int  打开串口的文件句柄
 *@param  speed  类型 int  串口速度
 *@return  void
 */
int speed_arr[] = {B115200, B38400, B19200, B9600, B4800, B2400, B1200, B300,
		B38400, B19200, B9600, B4800, B2400, B1200, B300, };
int name_arr[] = {115200, 38400,  19200,  9600,  4800,  2400,  1200,  300, 38400,
		19200,  9600, 4800, 2400, 1200,  300, };
int set_speed(int fd, int speed){
	int i;
	int status;
	struct termios Opt;

	for (i= 0; i < sizeof(speed_arr) / sizeof(int); i++) {
		if  (speed == name_arr[i]) {
			tcflush(fd, TCIOFLUSH);
			cfsetispeed(&Opt, speed_arr[i]);
			cfsetospeed(&Opt, speed_arr[i]);
			status = tcsetattr(fd, TCSANOW, &Opt);
			if  (status != 0) {
				perror("tcsetattr fd");
				return -1;
			}
			tcflush(fd,TCIOFLUSH);
		}
	}
	return 1;
}

/**
 *
 *  Configure device
 */
int configureDevice(int fd, int speed, int flags) {
	int err = 0;
	struct termios curr_term;
	tcflush(fd, TCIOFLUSH);
	if ((err = tcgetattr(fd, &curr_term)) != 0) {
		//printf("scanner: tcgetattr(%d) = %d,  errno %d\r\n", fd, err, errno);
		close(fd);
		return (-1);
	}

#if 1
	curr_term.c_iflag &= ~(IGNBRK | BRKINT | PARMRK | ISTRIP | INLCR | IGNCR
			| ICRNL | IXON);
	curr_term.c_oflag &= ~OPOST;
	curr_term.c_lflag &= ~(ECHO | ECHONL | ICANON | ISIG | IEXTEN);
#endif

	if ( flags == 0 ) {
		curr_term.c_iflag &= ~INPCK;
		curr_term.c_cflag &= ~(CSIZE | PARODD);
		curr_term.c_cflag |= (CS7 | PARENB);
		LOGD("TKLY========================flgs==0");
		//		curr_term.c_iflag &= ~INPCK;
		//		curr_term.c_cflag &= ~(CSIZE | PARENB);
		//		curr_term.c_cflag |= CS8;
	} else if ( flags == 1 ) {
		//		curr_term.c_iflag |= INPCK;
		//		curr_term.c_cflag &= ~(CSIZE | PARODD);
		//		curr_term.c_cflag |= CS8;

		//		curr_term.c_cflag |= (CS8 | PARENB);

		curr_term.c_iflag &= ~INPCK;
		curr_term.c_cflag &= ~(CSIZE | PARENB);
		curr_term.c_cflag |= CS8;
	}
	//	curr_term.c_cflag &= ~CRTSCTS;


	if(tcsetattr(fd, TCSANOW, &curr_term)!=0){
		LOGD("TKLY-tcsetattr不等0！");
	}

	tcflush(fd, TCIOFLUSH);
	if(tcsetattr(fd, TCSANOW, &curr_term)!=0){
	   LOGD("TKLY-tcsetattr(fd, TCSANOW, &curr_term)不等0！");
	}

	tcflush(fd, TCIOFLUSH);
	tcflush(fd, TCIOFLUSH);

	cfmakeraw(&curr_term);
	if (cfsetispeed(&curr_term, speed)) {
		close(fd);
		return (-1);
	}

	if (cfsetospeed(&curr_term, speed)) {
		close(fd);
		return (-1);
	}
	tcsetattr(fd, TCSANOW, &curr_term);
	return 1;
}

int configureScanner(int fd, int baudrate) {
	int err = 0;
	struct termios cfg;
	cfmakeraw(&cfg);
	tcflush(fd, TCIOFLUSH);
	if ((err = tcgetattr(fd, &cfg)) != 0) {
		//printf("scanner: tcgetattr(%d) = %d,  errno %d\r\n", fd, err, errno);
		close(fd);
		return (-1);
	}
	LOGD("c_cflag old = 0x%X", cfg.c_cflag);

	speed_t speed;
	speed = getBaudrate(baudrate);
	if (speed == -1) {
		/* TODO: throw an exception */
		LOGE("Invalid baudrate");
		return -1;
	}

	cfsetispeed(&cfg, speed);
	cfsetospeed(&cfg, speed);
#if 1
	cfg.c_cflag |= CLOCAL;
	//cfg.c_cflag |= CREAD;

	//cfg.c_cflag &= ~CLOCAL;
	cfg.c_cflag &= ~CREAD;

	//校验 停止位
	cfg.c_cflag &= ~PARENB;
	cfg.c_cflag &= ~CSTOPB;

	//硬件流控.
	cfg.c_cflag &= ~CRTSCTS;
	//cfg.c_cflag |= CRTSCTS;

	//数据位
	cfg.c_cflag &= ~CSIZE;
	cfg.c_cflag |= CS8;

	LOGE("c_cflag new = 0x%X", cfg.c_cflag);

	//软件流控
	cfg.c_iflag &= ~(IXON | IXOFF | IXANY);

	//disable cr lf swape
	cfg.c_iflag &= ~(INLCR | ICRNL | IGNCR);
	//cfg.c_oflag &= ~(ONLCR | OCRNL);
	cfg.c_oflag = 0;
	//直接发送
	//cfg.c_lflag &= ~ (ICANON | ECHO | ECHOE | ISIG);
	cfg.c_lflag = 0;
#endif
	if (tcsetattr(fd, TCSANOW, &cfg)) {
		LOGE("tcsetattr() failed");
		close(fd);
		return -1;
	}

	return 1;

}

jboolean  state = 0;
static jint releaseScannerSerial(JNIEnv *env, jint flag) {

	jclass strClass = (*env)->FindClass(env, "android/device/ScanManager");
	if(strClass == NULL) {
		LOGE("find android/device/ScanManager error \n");
		return -2;
	}


	jmethodID ctorID = (*env)->GetMethodID(env, strClass, "<init>", "()V");
	if(ctorID == NULL) {
		LOGE("find android/device/ScanManager init methodid error \n");
		return -2;
	}
	jobject obj = (*env)->NewObject(env, strClass, ctorID);
	if(obj == NULL) {
		LOGE(" android/device/ScanManager NewObject error \n");
		return -2;
	}
	if(flag == 1) {
		jmethodID scanStateID = (*env)->GetMethodID(env, strClass, "getScannerState", "()Z");
		if(scanStateID == NULL) {
			LOGE(" android/device/ScanManager get getScannerState method id error \n");
			return -2;
		}

		state = (*env)->CallBooleanMethod(env, obj, scanStateID);
		LOGD(" current scanner =-==================================%d\n", state);
		if(state == 1) {
			jmethodID closeID = (*env)->GetMethodID(env, strClass, "closeScanner", "()Z");
			if(closeID == NULL) {
				LOGE(" android/device/ScanManager get closeScanner method id error \n");
				return -2;
			}
			(*env)->CallBooleanMethod(env, obj, closeID);

			LOGD(" close scanner ================================== ok\n");
		}
	} else {
		if(state == 1) {
			jmethodID openID = (*env)->GetMethodID(env, strClass, "openScanner", "()Z");
			if(openID == NULL) {
				LOGE(" android/device/ScanManager get openScanner method id error \n");
				return -2;
			}
			(*env)->CallBooleanMethod(env, obj, openID);

			LOGD(" open scanner ================================== ok\n");
		}
	}

	return 0;

}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT jobject JNICALL Java_android_1serialport_1api_SerialPort_open
(JNIEnv *env, jclass thiz, jstring path, jint baudrate, jint flags)
{

	int fd;
	speed_t speed;
	jobject mFileDescriptor;
	int err;
	struct termios curr_term;
	LOGI("------------------Java_android_serialport_SerialPort_open--------");
	/* Check arguments */


	{		speed = getBaudrate(baudrate);
	if (speed == -1) {			/* TODO: throw an exception */
		LOGE("Invalid baudrate");
		return NULL;
	}
	}
	/* Opening device */
	{
		jboolean iscopy;
		const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
		LOGD("Opening serial port %s with flags 0x%x", path_utf, O_RDWR | flags);
		fd = open(path_utf, O_RDWR | flags);		LOGD("open() fd = %d", fd);
		(*env)->ReleaseStringUTFChars(env, path, path_utf);
		if (fd == -1)		{			/* Throw an exception */
			LOGE("Cannot open port");			/* TODO: throw an exception */
			return NULL;
		}
	}


	int config = configureDevice(fd, speed, flags);
	LOGD("config = %d", config);

	/* Create a corresponding file descriptor */
	{		jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
	jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
	jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
	mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
	(*env)->SetIntField(env, mFileDescriptor, descriptorID, (jint)fd);
	}


	return mFileDescriptor;
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_android_1serialport_1api_SerialPort_close
(JNIEnv *env, jobject thiz)
{
	jclass SerialPortClass = (*env)->GetObjectClass(env, thiz);
	jclass FileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");
	jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
	jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass, "descriptor", "I");
	jobject mFd = (*env)->GetObjectField(env, thiz, mFdID);
	jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);
	LOGD("close(fd = %d)", descriptor);
	close(descriptor);
}

