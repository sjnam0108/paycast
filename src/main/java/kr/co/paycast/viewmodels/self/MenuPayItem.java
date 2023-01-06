package kr.co.paycast.viewmodels.self;


public class MenuPayItem  {
	
	private String id ;	//장바구니 List ID
	private int menuId ;	//메뉴 ID
	private String compSelect ;	//메뉴 선택 값
	private String name; //메뉴 상품 명	
	private String orderCount; //메뉴 주문 수량
	private String price; //메뉴 가격	
	private String toPrice; // 메뉴 가격 + 옵션총가격
	private String imgSrc; //이미지 경로
	private String packing; //메뉴 포장 여부
	private String essVal; //필수 선택
	private String essName;
	private String essNameText;
	private String addVal; //추가 선택
	private String addName;
	private String addNameText;
	
	private String optionHtml;
	private String optionText;
	private String subMenu;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getCompSelect() {
		return compSelect;
	}
	public void setCompSelect(String compSelect) {
		this.compSelect = compSelect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getToPrice() {
		return toPrice;
	}
	public void setToPrice(String toPrice) {
		this.toPrice = toPrice;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public String getEssVal() {
		return essVal;
	}
	public void setEssVal(String essVal) {
		this.essVal = essVal;
	}
	public String getEssName() {
		return essName;
	}
	public void setEssName(String essName) {
		this.essName = essName;
	}
	public String getEssNameText() {
		return essNameText;
	}
	public void setEssNameText(String essNameText) {
		this.essNameText = essNameText;
	}
	public String getAddVal() {
		return addVal;
	}
	public void setAddVal(String addVal) {
		this.addVal = addVal;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public String getAddNameText() {
		return addNameText;
	}
	public void setAddNameText(String addNameText) {
		this.addNameText = addNameText;
	}
	public String getOptionHtml() {
		return optionHtml;
	}
	public void setOptionHtml(String optionHtml) {
		this.optionHtml = optionHtml;
	}
	public String getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	
}
