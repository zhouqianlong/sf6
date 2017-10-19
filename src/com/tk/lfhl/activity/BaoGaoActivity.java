package com.tk.lfhl.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.android.db.DBHelper;
import com.android.utils.MyToask;
import com.android.utils.ProgressDialogUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tk.adapter.BaoGaoGasDateAdapter;
import com.tk.lfhl.bean.GasDate;
import com.tk.lfhl.bean.RizhiInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BaoGaoActivity extends Activity implements android.view.View.OnClickListener{
	protected static final int CLOSE_DIALOG = 101;//关闭等待
	protected static final int SUCCE = 102;//生成成功
	private String [] type = {"梯形巷道","矩形巷道","半圆拱巷道","三心拱巷道"};
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	private BaoGaoGasDateAdapter gasDateAdapter;
	private String times ;
	DBHelper db;
	private RizhiInfo rizhiInfo;
	private String imagename;
	private TextView tv_title;
	private TextView ts_baogao_name,tv_baogao_name;//巷道名称
	private TextView ts_baogao_type,tv_baogao_type;//支护类型
	private TextView ts_baogao_mianji,tv_baogao_mianji;//面积
	private TextView ts_road_shap,tv_road_shap;//形状
	private TextView ts_liuliang,tv_liuliang;//流量
	private TextView ts_baogao_loufen,tv_baogao_loufen;//漏风方式
	private TextView ts_baogao_people,tv_baogao_people;//测人
	private TextView ts_baogao_time,tv_baogao_time;//测日
	private TextView ts_windSpeed,tv_windSpeed;//风速
	private TextView ts_hole_length,tv_hole_length;//井巷周界长度
	private TextView tv_lfv;//漏风率
	private TextView tv_lfl;//漏风率
	private TextView ts_image;
	private ImageView tv_image;
	private Button btn_back;
	private Button btn_delete;
	private Button btn_pdf;//生成pdf
	private ListView lv_more_gas;//多次记录的浓
	private int position = -1;
	private float mj = 0;//面积
	private float q = 0;//气体释放流量
	private float minDistance = 0 ;//最小距离
	private DecimalFormat df = new DecimalFormat("0.00");
	public int first = -1;//点击的第一个气体
	public int last = -1;//点击的第二个气体
	private Button btn_reset;//重新勾选
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baogao);
		View include = findViewById(R.id.il_title);
		tv_title = (TextView) include.findViewById(R.id.tv_title);
		tv_title.setText("检测报告");
		db = new DBHelper(getApplicationContext());
		rizhiInfo = (RizhiInfo) getIntent().getExtras().getSerializable("RizhiInfo");
		initView();
		if(rizhiInfo.getList().size()==2){
			calLFVLF();
		}
		System.out.println(rizhiInfo);
	}
	private void initView() {
		ts_baogao_name = (TextView) findViewById(R.id.ts_baogao_name);
		ts_baogao_type = (TextView) findViewById(R.id.ts_baogao_type);
		ts_baogao_mianji = (TextView) findViewById(R.id.ts_baogao_mianji);
		ts_road_shap = (TextView) findViewById(R.id.ts_road_shap);
		ts_liuliang = (TextView) findViewById(R.id.ts_liuliang);
		ts_baogao_loufen = (TextView) findViewById(R.id.ts_baogao_loufen);
		ts_baogao_people = (TextView) findViewById(R.id.ts_baogao_people);
		ts_baogao_time = (TextView) findViewById(R.id.ts_baogao_time);
		ts_windSpeed = (TextView) findViewById(R.id.ts_windSpeed);
		ts_hole_length = (TextView) findViewById(R.id.ts_hole_length);
		tv_lfv = (TextView) findViewById(R.id.tv_lfv);
		tv_lfl = (TextView) findViewById(R.id.tv_lfl);
		ts_image = (TextView) findViewById(R.id.ts_image);
		lv_more_gas = (ListView) findViewById(R.id.lv_more_gas);
		btn_reset = (Button) findViewById(R.id.btn_reset);
		
//
		tv_baogao_name = (TextView) findViewById(R.id.tv_baogao_name);
		tv_baogao_type = (TextView) findViewById(R.id.tv_baogao_type);
		tv_baogao_mianji = (TextView) findViewById(R.id.tv_baogao_mianji);
		tv_road_shap = (TextView) findViewById(R.id.tv_road_shap);
		tv_liuliang = (TextView) findViewById(R.id.tv_liuliang);
		tv_baogao_loufen = (TextView) findViewById(R.id.tv_baogao_loufen);
		tv_baogao_people = (TextView) findViewById(R.id.tv_baogao_people);
		tv_baogao_time = (TextView) findViewById(R.id.tv_baogao_time);
		tv_windSpeed = (TextView) findViewById(R.id.tv_windSpeed);
		tv_hole_length = (TextView) findViewById(R.id.tv_hole_length);
		tv_image = (ImageView) findViewById(R.id.tv_image);
		btn_back = (Button) findViewById(R.id.btn_back);

		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_pdf = (Button) findViewById(R.id.btn_pdf);
		btn_back.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_pdf.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
		setViewText();//设置内容
		setImageView(imagename);//设置图片
		if(rizhiInfo.getList()!=null){
			gasDateAdapter =  new BaoGaoGasDateAdapter(BaoGaoActivity.this,rizhiInfo.getList());
			lv_more_gas.setAdapter(gasDateAdapter);
		}
		lv_more_gas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(first==-1){
					first =position;
					tv_lfv.setText("已勾选序号"+(position+1));
					tv_lfl.setText("请继续勾选序号");
				}else{
					if(first>=position){
						Toast.makeText(getApplicationContext(), "只可选着大于第"+(first+1)+"次测试的气体。", 0).show();
					}else{
						last = position;
						calLfvLf(first,last);
					}
				}

				
				Toast.makeText(getApplicationContext(), "_"+rizhiInfo.getList().get(position).getGasDate(), 0).show();
			}
		});
	}

	private void setViewText() {
		if(tv_baogao_name!=null){
			position = Integer.valueOf(rizhiInfo.getROAD_SHAPE());
			imagename = rizhiInfo.getLOUFENG_TYPE();
			tv_baogao_name.setText(rizhiInfo.getROADWAY_NAME());
			tv_baogao_type.setText(rizhiInfo.getSUPPORTING_FORM());
			tv_road_shap.setText(type[position]);
			calcArea();
			calcQ();
			calcMindistance();
			tv_baogao_loufen.setText(imagename);
			tv_baogao_people.setText(rizhiInfo.getTEST_NAME());
			tv_windSpeed.setText(rizhiInfo.getWindSpeed()+"m/s");
//			tv_hole_length.setText(rizhiInfo.getHole_length()+"m");
			times = sdf.format(new Date(Long.valueOf(rizhiInfo.getTEST_TIME())));
			tv_baogao_time.setText(times);
		}
	} 
	private void calLfvLf(int aa,int bb) {
		if(rizhiInfo.getList().size()>=2){
			float a = rizhiInfo.getList().get(aa).getGasDate();
			float b = rizhiInfo.getList().get(bb).getGasDate();
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			if(a==0){
				tv_lfv.setText("("+(aa+1)+":"+(bb+1)+")漏风率 0%");
			}else{
				tv_lfv.setText("("+(aa+1)+":"+(bb+1)+")漏风率 "+decimalFormat.format((a-b)/a)+"%");
			}
			if(a*b==0){
				tv_lfl.setText("漏风量 0m3/分钟");
			}else{
				tv_lfl.setText("漏风量"+decimalFormat.format((a-b)/a *b)+"m3/分钟");
			}
			
		}
		
	}
	
	public void calLFVLF(){
		float a = rizhiInfo.getList().get(0).getGasDate();
		float b = rizhiInfo.getList().get(1).getGasDate();
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		if(a==0){
			tv_lfv.setText("1-2漏风率 0%");
		}else{
			tv_lfv.setText("1-2漏风率 "+decimalFormat.format((a-b)/a)+"%");
		}
		if(a*b==0){
			tv_lfl.setText("漏风量 0m3/分钟");
		}else{
			tv_lfl.setText("漏风量"+decimalFormat.format((a-b)/a *b)+"m3/分钟");
		}
		
	}
	/**
	 * 计算面积
	 */
	public void calcArea(){
		float height = 0;
		float width = 0;
		try {
			height = Float.valueOf(rizhiInfo.getROAD_HEIGHT());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			width = Float.valueOf(rizhiInfo.getROAD_WIDTH());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(position==0||position==1){//矩形和梯形S=H*B
			mj = height*width;
		}else if(position==2){//半圆
			mj = width*(height-0.11f*width);
		}else{//三心
			mj = width*(height-0.07f*width);
		}
		tv_baogao_mianji.setText(df.format(mj)+"m2");
	}
	/**
	 * 计算气体释放流量
	 */
	public void calcQ(){
		//5* 10-6 *S*V *103
		float speed = 0;
		try {
			speed = Float.valueOf(rizhiInfo.getWindSpeed());
		} catch (NumberFormatException e) {
			speed=0;
		}
		q = 5*0.00001f*speed*1000*mj;
		tv_liuliang.setText(df.format(q)+"L/min");
		
		
	}
	/**
	 * 计算最小距离 L=32*S/U;
	 */
	public void calcMindistance(){
		double u = Math.sqrt(mj);
		if(position==0||position==1){//梯形  矩形 
			u = u *4.16;
		}else if(position==2){//三心
			u = u *4.10;
		} else{
			u = u *3.84;
		}
		if(u==0){
			minDistance = 0;
		}else{
			
			minDistance =  (float) ((float) (32f*mj)/u); 
		}
		tv_hole_length.setText(df.format(minDistance)+"m");
		
	}
	@SuppressLint("HandlerLeak")
	private Handler mhander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==CLOSE_DIALOG){
				ProgressDialogUtils.dismissProgressDialog();
			}
			if(msg.what==SUCCE){
				Toast.makeText(BaoGaoActivity.this, "生成成功！"+Environment.getExternalStorageDirectory()+"/"+rizhiInfo.getROADWAY_NAME()+""+times+"-"+rizhiInfo.getTEST_NAME()+".pdf", Toast.LENGTH_LONG).show();
			}
		};
	};

	public  void  setImageView(String imagename){
		if ("正压漏风".equals(imagename)) {
			tv_image.setImageResource(R.drawable.localpositivepressure);
		}
		if ("负压漏风".equals(imagename)) {
			tv_image.setImageResource(R.drawable.localnegativepressure);
		}
//		if ("连续正压漏风".equals(imagename)) {
//			tv_image.setImageResource(R.drawable.continuouspositivepressure);
//		}
//		if ("连续负压漏风".equals(imagename)) {
//			tv_image.setImageResource(R.drawable.continuousnegativepressure);
//		}
	}
	@Override
	public void onClick(View v) {
		if(v==btn_back){
			finish();
		}
		if(v==btn_delete){
			new AlertDialog.Builder(BaoGaoActivity.this).setMessage("是否删除此次报告?").setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					db.deleteRIZHI_INFO(rizhiInfo);
					finish();
				}
			}).setNegativeButton("取消", null).show();
		}
		if(v==btn_reset){
			first = -1;
			last =-1;
			tv_lfv.setText("请勾选第一个检测点");
			tv_lfl.setText("");
		}
		if(v==btn_pdf){
			new AlertDialog.Builder(BaoGaoActivity.this).setMessage("是否生成PDF文件?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ProgressDialogUtils.showProgressDialog(BaoGaoActivity.this, "正在生成PDF文件请等待...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							Document doc=new Document();
							FileOutputStream fos;
							try {
								Log.i("TTT", "Start");
								String filetime =sdf2.format(new Date(Long.valueOf(rizhiInfo.getTEST_TIME())));
								String name = rizhiInfo.getROADWAY_NAME()+""+filetime+"-"+rizhiInfo.getTEST_NAME()+".pdf";
								//										fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/byte.pdf"));
								fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/"+name));
								PdfWriter.getInstance(doc, fos);
								doc.open ();  
								doc.setPageCount(1);
								Paragraph title =new Paragraph(tv_title.getText().toString(), setChineseFont(24));
								title.setAlignment(Element.ALIGN_CENTER);
								doc.add(title);//pdf标题
								Paragraph message =new Paragraph(ts_baogao_name.getText().toString()+tv_baogao_name.getText().toString(), setChineseFont(12));
								doc.add(message);// 
								message =new Paragraph("  "+ts_baogao_type.getText().toString()+tv_baogao_name.getText().toString(), setChineseFont(12));
								doc.add(message);// 
								message =new Paragraph("  "+ts_baogao_mianji.getText().toString()+tv_baogao_mianji.getText().toString()+"m2",setChineseFont(12));
								doc.add(message);// 
								message =new Paragraph("  "+ts_road_shap.getText().toString()+tv_road_shap.getText().toString(),setChineseFont(12));
								doc.add(message);// 
								message =new Paragraph("  "+ts_liuliang.getText().toString()+tv_liuliang.getText().toString(),setChineseFont(12));
								doc.add(message);// 
								message =new Paragraph(ts_baogao_loufen.getText().toString()+tv_baogao_loufen.getText().toString(), setChineseFont(12));
								doc.add(message);// 
								message =new Paragraph(ts_image.getText().toString(), setChineseFont(12));
								doc.add(message);//
								message =new Paragraph(" ", setChineseFont(12));
								doc.add(message);//换行
								PdfPTable table = new PdfPTable(2);  
								table.setHorizontalAlignment(Element.ALIGN_LEFT);
								table.setTotalWidth(300);
								table.setLockedWidth(true);
								table.addCell(new Paragraph("测试点",setChineseFont(12)));  
								table.addCell(new Paragraph("SF6浓度",setChineseFont(12)));  
								List<GasDate> gaslist = rizhiInfo.getList();
								for(int i = 0; i <gaslist.size();i++){
									Log.i("SF6","PDF生成"+gaslist.get(i).getMsg());
									table.addCell(new Paragraph(gaslist.get(i).getMsg(),setChineseFont(12)));  
									table.addCell(gaslist.get(i).getGasDate()+"ppm");  
								}
								doc.add(table);
								message =new Paragraph(tv_lfv.getText().toString()+tv_lfl.getText().toString(), setChineseFont(12));
								doc.add(message); 
								message =new Paragraph(ts_hole_length.getText().toString()+tv_hole_length.getText().toString(), setChineseFont(12));
								message =new Paragraph(ts_windSpeed.getText().toString()+tv_windSpeed.getText().toString(), setChineseFont(12));
								doc.add(message); 
								message =new Paragraph(ts_hole_length.getText().toString()+tv_hole_length.getText().toString(), setChineseFont(12));
								doc.add(message); 
								//								Resources res=getResources();
								//								Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
								Image image = Image.getInstance(Bitmap2Bytes(drawable2Bitmap(tv_image.getDrawable())));
								image.setAlignment(Element.ALIGN_LEFT);
								image.scaleAbsolute(314, 133);
								doc.add(image); 
								message =new Paragraph(ts_baogao_people.getText().toString()+tv_baogao_people.getText().toString(), setChineseFont(12));
								message.setAlignment(Element.ALIGN_RIGHT);
								doc.add(message); 
								message =new Paragraph(ts_baogao_time.getText().toString()+tv_baogao_time.getText().toString(), setChineseFont(12));
								message.setAlignment(Element.ALIGN_RIGHT);
								doc.add(message); 
								Log.i("TTT", "ImgSucce");
								//�?定要记得关闭document对象
								doc.close();
								fos.flush();
								fos.close();
								mhander.sendEmptyMessage(CLOSE_DIALOG);
								mhander.sendEmptyMessage(SUCCE);
								Log.i("TTT", "END");
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
								mhander.sendEmptyMessage(CLOSE_DIALOG);
							} catch (DocumentException e) {
								e.printStackTrace();
								mhander.sendEmptyMessage(CLOSE_DIALOG);
							} catch (IOException e) {
								e.printStackTrace();
								mhander.sendEmptyMessage(CLOSE_DIALOG);
							}
						}
					}).start();

				}
			}).setNegativeButton("取消", null).show();
		}
	}



	/**
	 * Bitmap �? byte[]
	 * @param bm
	 * @return
	 */
	private byte[] Bitmap2Bytes(Bitmap bm){

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

		return baos.toByteArray();  
	}


	private Bitmap drawable2Bitmap(Drawable drawable) {
		if(drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}else if(drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		}
		return null;
	}
	// 产生PDF字体
	public static Font setChineseFont(int size) {
		BaseFont bf = null;
		Font fontChinese = null;
		try {
			bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			fontChinese = new Font(bf, size, Font.NORMAL);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fontChinese;
	}
}
