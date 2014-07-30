package game.redapple1900.resistance;

public class DataSet {
	static final int SOILDER=100;
	static final int SPY=-100;
		static int[] NumOfTotalPlayers={5,6,7,8,9,10};
		static int[] NumOfMinPass={0,0,0,0,0,3,4,4,5,5,6};
		static int[] NumOfSpyPlayers={2,2,3,3,3,4};
		static int[] NumOfResistancePlayers={3,4,4,5,6,6};
		static int[][] NumOfMembersPerMission={{},{},{},{},{},{2,3,2,3,3},{2,3,4,3,4},{2,3,3,4,4},
			{3,4,4,5,5},{3,4,4,5,5},{3,4,4,5,5}};	
		static   String prolist[][]={{ "五人局","六人局","七人局","八人局","九人局","十人局" },{"第一轮","第二轮","第三轮","第四轮","第五轮"},{"0","1","2","3","4","5","6"},{"0","1","2","3","4","5","6"}};
		static 	String status_bottom_Main[]={"发牌","组队","投票","执行","失败","成功","等待"};
		
		static String status_button_positive_Main[]={"提名组队","提名通过","\n任务结果\n"};
		static String status_button_negative_Main[]={"不显示","提名否决","提名否决"};
		
		
	
	//DataSet could be inputed from files.
}

