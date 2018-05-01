/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ascii.art.unicodesymbol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.favorite.localdata.DatabasePresenter;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.utils.FileUtil;
import com.duy.ascii.art.utils.ShareUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Duy on 9/27/2017.
 */

public class SymbolFragment extends SimpleFragment {

    private ArrayList<String> symbols = new ArrayList<>();
    private EditText mEditInput;
    private DatabasePresenter mDatabasePresenter;
    private RecyclerView mRecyclerView;
    private SymbolAdapter mSymbolAdapter;

    public static SymbolFragment newInstance() {

        Bundle args = new Bundle();

        SymbolFragment fragment = new SymbolFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_symbol;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDatabasePresenter = new DatabasePresenter(getContext(), null);

        parseData();

        mEditInput = (EditText) findViewById(R.id.edit_input);
        Button btnCopy = (Button) findViewById(R.id.btn_copy);

        mSymbolAdapter = new SymbolAdapter(getContext(), symbols);
        mSymbolAdapter.setListener(new SymbolClickListener() {
            @Override
            public void onClick(String text) {
                mEditInput.getEditableText().insert(Math.max(mEditInput.getSelectionStart(), 0), text);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mRecyclerView.setAdapter(mSymbolAdapter);

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManagerCompat manager = ClipboardManagerCompatFactory.getManager(getContext());
                manager.setText(mEditInput.getText());
                Toast.makeText(getContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(mEditInput.getText().toString(), getContext());
            }
        });

        findViewById(R.id.img_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditInput.getText().toString().isEmpty()) {
                    mDatabasePresenter.insert(new TextItem(mEditInput.getText().toString()));
                    Toast.makeText(getContext(), R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void parseData() {
        try {
            InputStream open = getContext().getAssets().open("symbol.txt");
            String string = FileUtil.streamToString(open);
            String[] split = string.split("\\s+");
            Collections.addAll(symbols, split);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
