package kr.co.paycast.viewmodels.self;


public class MenuItemOption  {
	private int id;

	private int menuGroupID; //메뉴그룹 ID
	
	private String name; //메뉴 명
	
	private String seq; //메뉴 순서
	
	private String image; //메뉴 이미지명
	
	private String imageLocation; //메뉴 이미지경로
	
	private long imageSize; //메뉴 이미지 사이즈
	
	private String imageOriName; //메뉴 원본 이미지 명
	
	private String price; //메뉴 가격
	
	private String exchange; //메뉴 통화 - 기본 : "won"
	
	private String option; //메뉴 옵션 (신메뉴 : 0 / 추천메뉴 : 1 / 재고 없음 : 2)
	
	private String description; //메뉴 설명
	
	private String useYn; //해당 메뉴에 대한 사용 여부


	public MenuItemOption() {}

	public MenuItemOption(int id, int menuGroupID, String name, String seq, String image, String imageLocation, long imageSize, String imageOriName, 
			String price, String exchange, String option, String description, String useYn) {
		this.id = id;
		this.menuGroupID = menuGroupID;
		this.name = name;
		this.seq = seq;
		this.image = image;
		this.imageLocation = imageLocation;
		this.imageSize = imageSize;
		this.imageOriName = imageOriName;
		this.price = price;
		this.exchange = exchange;
		this.option = option;
		this.description = description;
		this.useYn = useYn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMenuGroupID() {
		return menuGroupID;
	}

	public void setMenuGroupID(int menuGroupID) {
		this.menuGroupID = menuGroupID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public long getImageSize() {
		return imageSize;
	}

	public void setImageSize(long imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageOriName() {
		return imageOriName;
	}

	public void setImageOriName(String imageOriName) {
		this.imageOriName = imageOriName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

}
