package nss.entity;

/*
 * entity for rules and should be fitted to response
 */
public class Article{
    private String id;
    private int ruleNum;
    private int chapterNum;
    private String chapterName;
    private int sectionNum;
    private String sectionName;
    private int articleNum;
    private String articleName;
    private String articleContent;
    private double score;
    
    public Article(){}

    public String getId() {return id;}
    public int getRuleNum() {return ruleNum;}
    public int getChapterNum() {return chapterNum;}
    public String getChapterName() {return chapterName;}
    public int getSectionNum() {return sectionNum;}
    public String getSectionName() {return sectionName;}
    public int getArticleNum() {return articleNum;}
    public String getArticleName() {return articleName;}
    public String getArticleContent() {return articleContent;}
    public double getScore() {return score;}
    
    
    public void setId(String id) {this.id = id;}
    public void setRuleNum(int ruleNum) {this.ruleNum = ruleNum;}
    public void setChapterNum(int chapterNum) {this.chapterNum = chapterNum;}
    public void setChapterName(String chapterName) {this.chapterName = chapterName;}
    public void setSectionNum(int sectionNum) {this.sectionNum = sectionNum;}
    public void setSectionName(String sectionName) {this.sectionName = sectionName;}
    public void setArticleNum(int articleNum) {this.articleNum = articleNum;}
    public void setArticleName(String articleName) {this.articleName = articleName;}
    public void setArticleContent(String articleContent) {this.articleContent = articleContent;}
    public void setScore(double score) {this.score = score;}
    
    
    
}