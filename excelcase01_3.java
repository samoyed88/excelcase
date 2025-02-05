package excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.*;

public class excelcase01_3 {

	public static void main(String[] args) {
		// 建立Scanner物件
		Scanner sc = new Scanner(System.in);
		// 宣告ArrayList //Q:如果遇到不同組別數會需要重新修改
		ArrayList<String>[] number = new ArrayList[16];
		ArrayList<String>[] name = new ArrayList[16];
		ArrayList<String>[] number2 = new ArrayList[16];
		ArrayList<String>[] name2 = new ArrayList[16];
		for (int i = 0; i < 16; i++) {
			number[i] = new ArrayList<String>();
			number2[i] = new ArrayList<String>();
			name[i] = new ArrayList<String>();
			name2[i] = new ArrayList<String>();
		}
		String inputstr;//使用者輸入數字或n來指定行為
		int count = 1, num2 = 0, input;//count為抽籤次數,num2為隨機數與num相同,input為文字輸入轉數字
		// 從excel讀取組別學號姓名並建立成陣列
		read(name, name2, number, number2);
		do {
			System.out.print("輸入1可顯示目前名單\r\n" + "輸入2可設定請假者\r\n" + "輸入3隨機數字抽籤\r\n" + "輸入4指定數字抽籤\r\n" + "輸入5重現抽籤\r\n"
					+ "輸入6可指定第幾次抽籤名單\r\n" + "輸入7可將結果存入excel\r\n" + "輸入n退出\r\n");
			inputstr = sc.next();
			try {
				input = Integer.parseInt(inputstr);
			} catch (NumberFormatException e) {
				if(!inputstr.equalsIgnoreCase("n"))System.out.println("輸入錯誤請重新輸入");//輸入n以外皆顯示重新輸入
				continue;
			}
			switch (input) {
			case 1:
				show(name2, number2);
				break;
			case 2:
				System.out.println("請輸入請假或缺席者學號，如果輸入完成請輸入N或n");
				String str = sc.next();
				while (!str.equalsIgnoreCase("n")) {
					search(number2, str, name2);
					str = sc.next();
				}
				break;
			case 3:
				int num = (int) (Math.random() * 10);// 隨機一個1~10的數字
				num2 = num;
				do {
					System.out.println("第" + count + "次抽籤名單");
					random(name2, ++num);
					System.out.println("輸入任意字可重抽，N退出");
					str = sc.next();
					count++;
				} while (!str.equalsIgnoreCase("n"));
				break;
			case 4:
				System.out.println("請輸入一指定數字抽籤");
				random(name2, sc.nextInt());
				break;
			case 5:
				reappear(num2, count, name2, number2);
				break;
			case 6:
				System.out.println("請輸入數字來顯示第n次的抽籤結果");
				int numtime = sc.nextInt();
				random(name2, num2 + (numtime - 1));
				break;
			case 7:
				save(name2, number2, ++num2, count);
				break;
			default:
				System.out.println("輸入錯誤請重新輸入");
			}
		} while (!inputstr.equalsIgnoreCase("n"));
		// 關閉Scanner
		sc.close();
	}

	public static void read(ArrayList<String>[] name, ArrayList<String>[] name2, ArrayList<String>[] number,
			ArrayList<String>[] number2) {
		int group;
		try {
			// 使用Apache POI庫中的XSSFWorkbook類別來建立一個Excel工作簿的物件，並從指定的檔案路徑中讀取檔案內容
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(
					"C:\\Users\\befor\\OneDrive - Ming Chuan University\\Documents\\student0220.xlsx"));
			// 使用xssfWorkbook物件的getSheetAt方法來取得第一個工作表(sheet)的物件，並存入sheet變數中
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			// 使用sheet物件的getLastRowNum方法來取得工作表中的最大行數，並存入maxRow變數中
			int maxRow = sheet.getLastRowNum();
			// 使用for迴圈來遍歷工作表中的每一行(row)，從第0行開始，到最大行數結束，每次遞增1
			for (int row = 0; row <= maxRow; row++) {
				// 讀取組別
				XSSFCell groupcell = sheet.getRow(row).getCell(2);
				group = Integer.parseInt(groupcell.toString());
				// 讀取學號
				XSSFCell numcell = sheet.getRow(row).getCell(0);
				// 讀取姓名
				XSSFCell namecell = sheet.getRow(row).getCell(1);
				number[group - 1].add(numcell.toString());
				name[group - 1].add(namecell.toString());
				number2[group - 1].add(numcell.toString());
				name2[group - 1].add(namecell.toString());
			}
			// 如果發生IOException異常，則捕捉並印出異常的堆疊追蹤
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void show(ArrayList<String>[] name, ArrayList<String>[] number) {
		System.out.println("顯示所有人");
		for (int i = 0; i < name.length; i++) {
			System.out.println("第" + (i + 1) + "組：");
			for (int j = 0; j < name[i].size(); j++) {
				System.out.print(name[i].get(j) + " " + number[i].get(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void random(ArrayList<String>[] name, int num) {
		for (int i = 0; i < name.length; i++) {
			int a = num % name[i].size();// 隨機數除小組人數的餘數
			System.out.print("本次抽到的為：");
			System.out.println("第" + (i + 1) + "組" + name[i].get(a));
		}
	}

	public static void search(ArrayList<String>[] number, String input, ArrayList<String>[] name) {
		int num = -1;
		for (int i = 0; i < number.length; i++) {
			for (int j = 0; j < number[i].size(); j++) {
				num = number[i].indexOf(input);
				if (num != -1) {
					name[i].remove(num);
					number[i].remove(num);
					System.out.print("成功跳過請假者");
					return;// 結束此方法
				}
			}
		}
		System.out.println("查無此人，請重新輸入");
	}

	public static void reappear(int num, int count, ArrayList<String>[] name, ArrayList<String>[] number) {
		for (int i = 1; i < count; i++) {
			random(name, num++);
			System.out.println();
		}
	}

	public static void save(ArrayList<String>[] name, ArrayList<String>[] number, int num, int count) {
		// 保存文件的位置
		String filepath = "C:\\Users\\befor\\OneDrive - Ming Chuan University\\Documents\\save.xlsx";
		try (Workbook workbook = new XSSFWorkbook()) {
			// 創建新的工作表
			Sheet sheet = workbook.createSheet("students");
			int rowcount = 1, count_1 = 1;
			// 創建第一行（標題行）
			Row headerRow = sheet.createRow(0);
			// 寫入標題
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("學號");
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("姓名");
			Cell headerCell3 = headerRow.createCell(2);
			headerCell3.setCellValue("組別");
			for (int j = 1; j < count; j++) {
				Row test = sheet.createRow(rowcount++);
				Cell testCell1 = test.createCell(0);
				testCell1.setCellValue("第" + count_1 + "次");
				for (int i = 0; i < name.length; i++) {
					// 創建行
					Row row = sheet.createRow(rowcount++);
					// 寫入姓名和學號信息
					int a = num % name[i].size();
					// 創建單元格
					Cell nameCell = row.createCell(0);
					nameCell.setCellValue(name[i].get(a));

					Cell numberCell = row.createCell(1);
					numberCell.setCellValue(number[i].get(a));
					Cell groupCell = row.createCell(2);
					groupCell.setCellValue("第" + (i + 1) + "組");
				}
				num++;
				count_1++;
			}

			// 將工作簿寫入新的 Excel 文件
			try (FileOutputStream fileOut = new FileOutputStream(filepath)) {
				workbook.write(fileOut);
				System.out.println("新的 Excel 文件已保存成功！\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
