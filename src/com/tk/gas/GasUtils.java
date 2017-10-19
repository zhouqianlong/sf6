package com.tk.gas;

public class GasUtils {
	/**
	 * 
	 * 
	 *0x00C0 Modbus_address
	 *
	 *0x0080  给出连接的仪器型号
	 *
	 *0x0083  只可读
	 *
	 *0x0084  给出连接仪器的软件版本
	 *
	 *0x0085 只读
	 *
	 *0x0086 给出仪器唯一的序列号
	 *
	 *0x0089 只读	
	 *
	 *0x0005 给出气体浓度的设置值
	 *
	 *0x000A 在此寄存器内将气体浓度以数值形式存储。 根据检测气体不同，针对不同的smartMODULE型号，在随货时会提供单独的出厂质量说明，上面会有一个计算系数，用于计算出正确的气体浓度。
	 *
	 *0x0003 传感器内部温度，作为温度校准的参考数值
	 *
	 *0x0045 提供气体主报警的限值，可由用户设置。
	 *
	 *0x0044 提供气体主报警的限值，可由用户设置。
	 *
	 *0x0047 寄存器存储零点时检测到的强度。
	 *
	 *0x0009 此状态标记给出了模块可采用的状态信息 Read only / 只读 Individual flags, read from right to left, mean / 
	 *		 单个地址从右到左表示内容: 
	 *		 	Flag 0 Testflag value“1” with device test 值“1”表示仪器处于测试状态 
	 *			Flag 1 Warmup value “1”approx. 10s after start 值“1”表示启动后大约10秒预热 
	 *			Flag 2 Syserr value “1” System Error 值“1”表示系统错误 
	 *			Flag 3 Alarm value “1” if main gas alarm warning 值“1”表示气体主报警开始工作 
	 *			Flag 4 Warn value“1” if gas pre-alarm warning 气体预报警开始工作 
	 *			Flag 5 Startup value “1” in the start-up phase (less than 90sec) 值“1”表示处于启动阶段（至少90秒） 
	 *			Flag 6 Korr value “1” if smartMODULE is temperature-compensated 值“1”表示smartMODULE提供温度补偿功能 
	 *			Flag 7 mw_ok value “1” if zero point was set 值“1”表示零点已设定
	 *
	 *		    Flags 6 (Korr), 7 (mw_ok) 为内部信号，是在smartMODULE生产时设置。他们的值也作为质量检测用，当每个smartMODULE都完成了温度补偿和校准过程后，他们的值会被设为“1”
	 * 
	 */

}
