package com.mhj.longlivemypet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PetCalendarAdapter extends FirestoreRecyclerAdapter<PetCalendarItem, PetCalendarAdapter.MyViewHolderr> {
    View itemView;
    String document, title, body, write_date;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirestoreRecyclerOptions<PetCalendarItem> options;
    PetCalendarItemDetailListener listener;
    PetCalendarFragment petCalendarFragment;

    public PetCalendarAdapter(FirestoreRecyclerOptions<PetCalendarItem> options, PetCalendarItemDetailListener listener, PetCalendarFragment petCalendarFragment) {
        super(options);
        this.listener = listener;
        this.options = options;
        this.petCalendarFragment = petCalendarFragment;
    }


    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    protected void onBindViewHolder(@NonNull final PetCalendarAdapter.MyViewHolderr myViewHolderr, final int position, @NonNull final PetCalendarItem petCalendarItem) {
        myViewHolderr.textViewTitle.setText(petCalendarItem.getTitle());
        myViewHolderr.textViewBody.setText(petCalendarItem.getBody());


        //일정수정버튼(수정하기프레그먼트진입)
        myViewHolderr.button_Adjust_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document = getSnapshots().getSnapshot(position).getId();
                title = petCalendarItem.title;
                body = petCalendarItem.body;
                write_date = petCalendarItem.write_date;
                listener.petCalendaritemDetail(document, title, body, write_date);
            }
        });

        //펫삭제버튼
        myViewHolderr.button_Delete_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(myViewHolderr.itemView.getContext());
                builder.setMessage("삭제 하시겠습니까?");
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(myViewHolderr.itemView.getContext(), "삭제가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        document = getSnapshots().getSnapshot(myViewHolderr.getAdapterPosition()).getId();
                        firestore.collection("Calendar").document(document).delete();
                        Toast.makeText(myViewHolderr.itemView.getContext(),"일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        petCalendarFragment.onResume();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }//onBindViewHolder



    // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따른다.
    @NonNull
    @Override
    public MyViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()) ;
        itemView = inflater.inflate(R.layout.petcalendar_item,parent, false);
        return new MyViewHolderr(itemView);
    }//onCreateViewHolder


    // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
    public class MyViewHolderr extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewBody,textViewWrite_date;
        Button button_Delete_Calender, button_Adjust_Calender;

        public MyViewHolderr(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewBody = view.findViewById(R.id.textViewBody);
            textViewWrite_date = view.findViewById(R.id.textViewWrite_date);
            button_Delete_Calender = view.findViewById(R.id.button_Delete_Calender);
            button_Adjust_Calender = view.findViewById(R.id.button_Adjust_Calender);
        }// MyViewHolder
    }// class MyViewHolder


    interface PetCalendarItemDetailListener{
        void petCalendaritemDetail(String document,String title,String body,String write_date);
    }//PetCalendarItemDetailListener


}
