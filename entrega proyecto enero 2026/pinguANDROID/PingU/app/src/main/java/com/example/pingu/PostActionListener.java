package com.example.pingu;

import android.widget.TextView;

public interface PostActionListener {
    void onLike(PostMuro post);
    void onDislike(PostMuro post);
    void onEliminar(PostMuro post);
    void onResponder(PostMuro post);
    void onVerRespuestas(PostMuro post, TextView tvRespuestas);
}