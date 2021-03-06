package android.example.com.split.ui.tabfragment;

import android.content.Context;
import android.example.com.split.data.entity.Expense;
import android.example.com.split.data.entity.Group;
import android.example.com.split.data.entity.User;

import java.util.List;

public interface ExpensesActions {

  public void addExpense(Group group, Expense expense);
  public Expense getExpenseDetailFromUI(String title, double amount, String payerName);
  public void populateSpinnerWithMembers(Context context, List<User> users);
  public User selectPayer(Group group, int position);
  public User selectPayer(Group group, User payer);


  //Checks
  public boolean validateExpenseInput(String title, double amount, String payerName);


  //Actions
  public Expense initialiseNewExpense(String title, double amount, String payerName);
  public List<Double>  updateMembersBalance(User user, Group group, Expense expense);
  // total share is omited
  public void saveNewExpense(Expense expense);
  public void writeExpenseToRemote(Group group, Expense expense);
  public void updateUIWithNewExpense();

  public void fetchExpensesListFromRemote(Group group);

  public void fetchSingleExpenseFromRemote(Group group,Expense expense);

  public void removeExpenseFromRemote(Group group,Expense expense);

















}
