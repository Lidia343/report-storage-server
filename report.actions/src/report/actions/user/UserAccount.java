package report.actions.user;

public class UserAccount 
{
   private String username;
   private String password;

   public UserAccount(String a_username, String a_password) 
   {
      username = a_username;
      password = a_password;
   }
 
   public String getUserName() 
   {
      return username;
   }
 
   public String getPassword() 
   {
      return password;
   }
}