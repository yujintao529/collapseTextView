package com.github.yujintao529.collapsetextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.yujintao529.library.CollapseTextView;

public class MainActivity extends AppCompatActivity {

    private CollapseTextView collapseTextView;
    private CollapseTextView collapseTextView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collapseTextView= (CollapseTextView) findViewById(R.id.text1);
        collapseTextView.setText("我们认为下述真理是不言而喻的：人人生而平等，造物主赋予他们若干不可让与的权利，其中包括生存权、自由权和追求幸福的权利。为了保障这些权利，人们才在他们中间建立政府，而政府的正当权利，则是经被统治者同意授予的。任何形式的政府一旦对这些目标的实现起破坏作用时，人民便有权予以更换或废除，以建立一个新的政府。新政府所依据的原则和组织其权利的方式，务使人民认为唯有这样才最有可能使他们获得安全和幸福。若真要审慎的来说，成立多年的政府是不应当由于无关紧要的和一时的原因而予以更换的。");
        collapseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseTextView.toggle(false);
            }
        });
        collapseTextView2= (CollapseTextView) findViewById(R.id.text2);
        collapseTextView2.setText("我们认为下述真理是不言而喻的：人人生而平等，造物主赋予他们若干不可让与的权利，其中包括生存权、自由权和追求幸福的权利。为了保障这些权利，人们才在他们中间建立政府，而政府的正当权利，则是经被统治者同意授予的。任何形式的政府一旦对这些目标的实现起破坏作用时，人民便有权予以更换或废除，以建立一个新的政府。新政府所依据的原则和组织其权利的方式，务使人民认为唯有这样才最有可能使他们获得安全和幸福。若真要审慎的来说，成立多年的政府是不应当由于无关紧要的和一时的原因而予以更换的。");
        collapseTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseTextView2.toggle(true);
            }
        });
    }
}
