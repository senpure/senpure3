package com.senpure.ann;

import com.senpure.base.annotation.ExtPermission;
import org.junit.Test;
import org.springframework.stereotype.Controller;


@ExtPermission
public class AnnAdd {


    @Test
    public  void ann()
    {

        AnnAdd annAdd=new AnnAdd();
       // AnnotationsAttribute ann=new AnnotationsAttribute();

        System.out.println(annAdd.getClass().getAnnotation(ExtPermission.class));
        System.out.println(annAdd.getClass().getAnnotation(Controller.class));
    }

}
