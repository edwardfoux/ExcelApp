package com.example.edwardfouxvictorious.excelapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class GridActivity extends AppCompatActivity {

    private static final String COLUMNS_COUNT = "columns";
    private static final String ROWS_COUNT = "rows";
    private static final String MAP = "map";

    private static final int CELL_WIDTH_HEIGHT = 400;
    private static final int INITIAL_I_J = 1;
    private static final int UNKNOWN_I_J = -1;

    private View.OnClickListener clickListener = new ClickListener();
    private View.OnClickListener buttonClickListener = new ButtonClickListener();
    private EditText editText;

    private TableLayout tableLayout;

    private int selectedColumn = UNKNOWN_I_J;
    private int selectedRow = UNKNOWN_I_J;
    int column = INITIAL_I_J;
    int row = INITIAL_I_J;

    private HashMap<Integer, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        findViewById(R.id.add_column).setOnClickListener(buttonClickListener);
        findViewById(R.id.add_row).setOnClickListener(buttonClickListener);
        findViewById(R.id.refresh).setOnClickListener(buttonClickListener);
        findViewById(R.id.edit_cell).setOnClickListener(buttonClickListener);
        editText = (EditText) findViewById(R.id.edit_value);
        tableLayout = (TableLayout) findViewById(R.id.table);

        drawTable(column, row);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(COLUMNS_COUNT, column);
        outState.putInt(ROWS_COUNT, row);
        outState.putSerializable(MAP, hashMap);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        column = savedInstanceState.getInt(COLUMNS_COUNT);
        row = savedInstanceState.getInt(ROWS_COUNT);
        hashMap = (HashMap<Integer, String>) savedInstanceState.getSerializable(MAP);
        drawTable(column, row);
    }

    private void drawTable(int column, int row) {
        tableLayout.removeAllViews();
        for (int i = 0; i < row; i++) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.row_layout, null);
            for (int j = 0; j < column; j++) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.textview_layout, null);
                textView.setHeight(CELL_WIDTH_HEIGHT);
                textView.setWidth(CELL_WIDTH_HEIGHT);
                int id = i * column + j;
                textView.setId(id);
                if (hashMap.containsKey(id)) {
                    textView.setText(hashMap.get(id));
                } else {
                    textView.setText(String.valueOf(0));
                }
                textView.setOnClickListener(clickListener);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
    }

    private void addRow() {
        row++;
        drawTable(column, row);
    }

    private void addColumn() {
        column++;
        drawTable(column, row);
    }

    private void reset() {
        column = INITIAL_I_J;
        row = INITIAL_I_J;
        drawTable(column, row);
    }

    private void editCell() {
        String value = editText.getText().toString();
        if (TextUtils.isEmpty(value)) return;
        if (selectedColumn == UNKNOWN_I_J || selectedRow == UNKNOWN_I_J) return;

        int id = selectedRow * column + selectedColumn;
        ((TextView) findViewById(id)).setText(value);
        editText.setText("");

        hashMap.put(id, value);
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (selectedColumn > UNKNOWN_I_J && selectedRow > UNKNOWN_I_J) {
                findViewById(selectedRow * column + selectedColumn).setBackgroundColor(Color.WHITE);
            }
            int id  = v.getId();
            selectedRow = id / column;
            selectedColumn = id - (selectedRow * column);
            findViewById(selectedRow * column + selectedColumn).setBackgroundColor(Color.LTGRAY);

        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_column:
                    addColumn();
                    break;
                case R.id.add_row:
                    addRow();
                    break;
                case R.id.edit_cell:
                    editCell();
                    break;
                case R.id.refresh:
                    reset();
                    break;
            }
        }
    }
}
