package com.mhj.longlivemypet;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class PetAdapter extends FirestoreRecyclerAdapter<PetItem, PetAdapter.MyViewHolder> {

    View itemView;
    String document;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public PetAdapter(FirestoreRecyclerOptions<PetItem> options) {
        super(options);
    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position, @NonNull PetItem petItem) {
        myViewHolder.textViewName.setText(petItem.getName());
        myViewHolder.textViewSex.setText(petItem.getSex());
        myViewHolder.textViewBreed.setText(petItem.getBreed());
        myViewHolder.textViewDate.setText(petItem.getDate());
        myViewHolder.textViewWeight.setText(petItem.getWeight());
        myViewHolder.button_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document = getSnapshots().getSnapshot(position).getId();
                firestore.collection("Pet").document(document).delete();
                Toast.makeText(myViewHolder.itemView.getContext(), "나의 펫이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });


    }


    // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며,
    // extends의 Adater<>에서 <>안에들어가는 타입을 따른다.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()) ;
        itemView = inflater.inflate(R.layout.pet_item,parent, false);

        return new MyViewHolder(itemView);
    }


    // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, textViewSex, textViewBreed, textViewDate, textViewWeight;
        Button button_Delete;

        public MyViewHolder(View view){
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            textViewSex = view.findViewById(R.id.textViewSex);
            textViewBreed = view.findViewById(R.id.textViewBreed);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewWeight = view.findViewById(R.id.textViewWeight);
            button_Delete = view.findViewById(R.id.button_Delete);
        }
    }

    //리사이클러뷰안에 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


}
