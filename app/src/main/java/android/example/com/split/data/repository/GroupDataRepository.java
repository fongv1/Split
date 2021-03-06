package android.example.com.split.data.repository;

import android.example.com.split.data.entity.Group;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupDataRepository  {

  public static final String SUCCESS = "success";
  public static final String GROUP_ID = "group-id";
  public static final String USERS_LIST = "users-list";
  public static final String GROUP_LIST = "group-list";
  private static final String TAG = "DataRepository";
  private FirebaseFirestore db;

  // create new group
  public void addGroup(Group group, final Handler.Callback listener) {
    db = FirebaseFirestore.getInstance();
    final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    db.collection("groups").add(group)
      .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
        @Override
        public void onComplete(@NonNull Task<DocumentReference> task) {
          if (task.isSuccessful()) {
            DocumentReference documentReference = task.getResult();
            String documentId = documentReference.getId();

            // add the created user to the group
           // addCurrentMember(documentId, user);

            Message message = new Message();
            final Bundle data = new Bundle();
            data.putBoolean(SUCCESS, true);
            data.putString(GROUP_ID, documentId);

                    /*ArrayList<User> users = new ArrayList<>();
                    users.add(new User());
                    data.putSerializable(USERS_LIST, users);*/

            message.setData(data);
            listener.handleMessage(message);
          } else {
            Message message = new Message();
            final Bundle data = new Bundle();
            data.putBoolean(SUCCESS, false);
            message.setData(data);
            listener.handleMessage(message);
          }
        }
      });
  }

  private void addCurrentMember(String documentId, String user) {
    db.collection("groups").document(documentId).update("members", FieldValue.arrayUnion(user));
  }



  // add a member to a group
  public void addGroupMember(String groupId, String memberId, final Handler.Callback listener) {
    db = FirebaseFirestore.getInstance();
    DocumentReference groupDocument = db.collection("groups").document(groupId);
    // add new member to this group
    groupDocument.update("members", FieldValue.arrayUnion(memberId))
                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()) {
                       Message message = new Message();
                       final Bundle data = new Bundle();
                       data.putBoolean(SUCCESS, true);
                       message.setData(data);
                       listener.handleMessage(message);
                     } else {
                       Message message = new Message();
                       final Bundle data = new Bundle();
                       data.putBoolean(SUCCESS, false);
                       message.setData(data);
                       listener.handleMessage(message);
                     }
                   }
                 });
  }

  // add expense to a group
  // should be changed

  /* public void addGroupExpense(String groupId , Map<String, Object> expense , final Handler
  .Callback listener){
       db = FirebaseFirestore.getInstance();
       DocumentReference groupDocument = db.collection("groups").document(groupId);
       // add new expense to this group
       groupDocument.update("expenses",expense).addOnCompleteListener(new
       OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   Message message = new Message();
                   final Bundle data = new Bundle();
                   data.putBoolean(SUCCESS, true);
                   message.setData(data);
                   listener.handleMessage(message);
               } else{
                   Message message = new Message();
                   final Bundle data = new Bundle();
                   data.putBoolean(SUCCESS, false);
                   message.setData(data);
                   listener.handleMessage(message);
               }
           }
       });

   }*/
  // get the list of user's groups
  public void getGroupList(String userId, final Handler.Callback listener) {
    db = FirebaseFirestore.getInstance();
    final List<Group> groupList = new ArrayList<>();
    db.collection("groups").whereArrayContains("members", userId).get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
          Message message = new Message();
          final Bundle data = new Bundle();

          if (!queryDocumentSnapshots.isEmpty()) {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
              Group group = documentSnapshot.toObject(Group.class);
              //TODO fix the group id missing while saving a group
              group.setGroupId(documentSnapshot.getId());
              groupList.add(group);
            }
            data.putBoolean(SUCCESS, true);
            data.putSerializable(GROUP_LIST, (Serializable) groupList);
            message.setData(data);
            listener.handleMessage(message);
          } else {
            data.putBoolean(SUCCESS, false);
            message.setData(data);
            listener.handleMessage(message);
          }

        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Message message = new Message();
        final Bundle data = new Bundle();
        data.putBoolean(SUCCESS, false);
        message.setData(data);
        listener.handleMessage(message);
      }
    });
  }
// not correct method
  /*public void removeGroup(User currentUser, Group group, OnCompleteListener<DocumentSnapshot>
      listener) {
    db.collection("users").document(currentUser.getId()).collection("groups")
      .document(group.getGroupId()).get().addOnCompleteListener(listener);
  }*/

  public void removeGroup(Group group , final Handler.Callback listener ){
    db = FirebaseFirestore.getInstance();
    db.collection("groups").document(group.getGroupId())
      .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Message message = new Message();
        Bundle data = new Bundle();
        if(task.isSuccessful()) {
          data.putBoolean(SUCCESS, true);
          message.setData(data);
          listener.handleMessage(message);
        }
        else {
          data.putBoolean(SUCCESS, false);
          message.setData(data);
          listener.handleMessage(message);
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Message message = new Message();
        Bundle data = new Bundle();
        data.putBoolean(SUCCESS, false);
        message.setData(data);
        listener.handleMessage(message);
      }
    });

  }


 /* public void addGroup(User currentUser, Group group, OnCompleteListener<Void> listener) {
    db = FirebaseFirestore.getInstance();
    db.collection("groups").document().set(group).addOnCompleteListener(listener);
  }*/
}
