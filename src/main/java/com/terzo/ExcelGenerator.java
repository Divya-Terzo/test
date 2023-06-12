package com.terzo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelGenerator {

  public static void main(String[] args) {
    List<WorkflowModel> modelList = new ArrayList<>();
    List<AuditModel> auditModelList = new ArrayList<>();
    try {
      FileInputStream file =
          new FileInputStream(
              new File(
                  "/Users/ironhide/Downloads/Fiera Tenants - Workflow Pipeline Completion Timelines and Contract Renewal Data.xlsx"));

      // Create Workbook instance holding reference to .xlsx file
      XSSFWorkbook workbook = new XSSFWorkbook(file);

      // Get first/desired sheet from the workbook
      XSSFSheet sheet = workbook.getSheetAt(3);

      // Iterate through each rows one by one
      Iterator<Row> rowIterator = sheet.iterator();
      if (rowIterator.hasNext()) rowIterator.next();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        String tenantName =
            row.getCell(0, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        Long workflowId =
            Double.valueOf(
                    row.getCell(1, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK)
                        .getNumericCellValue())
                .longValue();
        String workFlowName =
            row.getCell(2, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        String pipeLineName =
            row.getCell(3, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        String status =
            row.getCell(4, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        Date startDate =
            row.getCell(5, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getDateCellValue();
        Date endDate =
            row.getCell(6, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getDateCellValue();
        WorkflowModel workflowModel =
            new WorkflowModel(
                tenantName, workflowId, workFlowName, pipeLineName, status, startDate, endDate);
        modelList.add(workflowModel);
      }
      file.close();
      Map<Long, WorkflowModel> workflowModelMap =
          modelList.stream()
              .collect(
                  Collectors.toMap(
                      WorkflowModel::getWorkflowId,
                      Function.identity(),
                      (e1, e2) -> e1,
                      LinkedHashMap::new));
      file = new FileInputStream(new File("/Users/ironhide/Downloads/workflowaudit.xlsx"));
      workbook = new XSSFWorkbook(file);

      // Get first/desired sheet from the workbook
      sheet = workbook.getSheetAt(0);

      // Iterate through each rows one by one
      rowIterator = sheet.iterator();
      if (rowIterator.hasNext()) rowIterator.next();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        String action =
            row.getCell(3, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        Long workflowId =
            Double.valueOf(
                    row.getCell(5, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK)
                        .getNumericCellValue())
                .longValue();
        Long propertyId =
            Double.valueOf(
                    row.getCell(6, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK)
                        .getNumericCellValue())
                .longValue();
        String propertyName =
            row.getCell(7, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        String propertyAction =
            row.getCell(8, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        String details =
            row.getCell(9, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
        String stageName = "";
        String newValue = "";
        String oldValue = "";
        JSONObject object = new JSONObject(details);
        if (object.has("newValue")) {
          JSONArray array = object.getJSONArray("newValue");
          if (array != null && !array.isEmpty()) {
            if (array.getJSONObject(0).has("name")) {
              newValue = array.getJSONObject(0).getString("name");
            }
          }
        }
        if (object.has("oldValue")) {
          JSONArray array = object.getJSONArray("oldValue");
          if (array != null && !array.isEmpty()) {
            if (array.getJSONObject(0).has("name")) {
              oldValue = array.getJSONObject(0).getString("name");
            }
          }
        }
        if (object.has("stageName")) {
          stageName = object.getString("stageName");
        }
        Date endDate =
            row.getCell(17, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getDateCellValue();
        AuditModel auditModel =
            new AuditModel(
                action,
                workflowId,
                propertyId,
                propertyName,
                propertyAction,
                stageName,
                oldValue,
                newValue,
                endDate);
        auditModelList.add(auditModel);
      }
      Map<Long, List<AuditModel>> auditModelMap = new HashMap<>();
      auditModelList.forEach(
          auditModel -> {
            List<AuditModel> propertyList;
            if (!auditModelMap.containsKey(auditModel.getWorkflowId())) {
              propertyList = new ArrayList<>();
              auditModelMap.put(auditModel.getWorkflowId(), propertyList);
            } else {
              propertyList = auditModelMap.get(auditModel.getWorkflowId());
            }
            propertyList.add(auditModel);
          });
      System.out.println(auditModelMap);
      file.close();
      System.out.println(workflowModelMap);

      XSSFWorkbook wb = new XSSFWorkbook();
      CreationHelper createHelper = wb.getCreationHelper();
      CellStyle cellStyle = wb.createCellStyle();
      cellStyle.setDataFormat(
              createHelper.createDataFormat().getFormat("yyyy-mm-dd h:mm:ss"));
      XSSFSheet outSheet = wb.createSheet();
      AtomicInteger rowIndex = new AtomicInteger();
      XSSFRow outRow = outSheet.createRow(rowIndex.get());
      AtomicInteger cellIndex = new AtomicInteger(-1);
      XSSFCell cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("TENANT");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("WORKFLOW ID");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("WORKFLOW NAME");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("PIPELINE NAME");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("STATUS");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("START DATE");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("END DATE");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("ACTIVITY ACTION");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("PROPERTY ID");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("PROPERTY NAME");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("PROPERTY ACTION");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("STAGE NAME");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("OLD VALUE");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("NEW NAME");
      cell = outRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
      cell.setCellValue("ACTIVITY TIMESTAMP");
      workflowModelMap
          .entrySet()
          .forEach(
              workflowEntry -> {
                WorkflowModel value = workflowEntry.getValue();
                if (auditModelMap.containsKey(workflowEntry.getKey())) {
                  auditModelMap
                      .get(workflowEntry.getKey())
                      .forEach(
                          auditModel -> {
                            XSSFRow dataOutRow = outSheet.createRow(rowIndex.incrementAndGet());
                            cellIndex.set(-1);
                            XSSFCell dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(value.getTenant());
                            dataCell =
                                dataOutRow.createCell(
                                    cellIndex.incrementAndGet(), CellType.NUMERIC);
                            dataCell.setCellValue(value.getWorkflowId());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(value.getWorkflowName());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(value.getPipelineName());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(value.getStatus());
                            dataCell =
                                dataOutRow.createCell(
                                    cellIndex.incrementAndGet(), CellType.NUMERIC);
                            if (value.getStartDate() != null) {
                              dataCell.setCellStyle(cellStyle);
                              dataCell.setCellValue(value.getStartDate());
                            }
                            dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.NUMERIC);
                            if (value.getEndDate() != null) {
                              dataCell.setCellStyle(cellStyle);
                              dataCell.setCellValue(value.getEndDate());
                            }

                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(auditModel.getAction());
                            dataCell =
                                dataOutRow.createCell(
                                    cellIndex.incrementAndGet(), CellType.NUMERIC);
                            dataCell.setCellValue(auditModel.getPropertyId());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(auditModel.getPropertyName());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(auditModel.getPropertyAction());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(auditModel.getStageName());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(auditModel.getOldValue());
                            dataCell =
                                dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                            dataCell.setCellValue(auditModel.getNewValue());
                            dataCell =
                                dataOutRow.createCell(
                                    cellIndex.incrementAndGet(), CellType.NUMERIC);
                            if (auditModel.getTimeStamp() != null) {
                              dataCell.setCellStyle(cellStyle);
                              dataCell.setCellValue(auditModel.getTimeStamp());
                            }
                          });
                } else {
                  XSSFRow dataOutRow = outSheet.createRow(rowIndex.incrementAndGet());
                  cellIndex.set(-1);
                  XSSFCell dataCell =
                      dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                  dataCell.setCellValue(value.getTenant());
                  dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.NUMERIC);
                  dataCell.setCellValue(value.getWorkflowId());
                  dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                  dataCell.setCellValue(value.getWorkflowName());
                  dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                  dataCell.setCellValue(value.getPipelineName());
                  dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.STRING);
                  dataCell.setCellValue(value.getStatus());
                  dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.NUMERIC);
                  if (value.getStartDate() != null) {
                    dataCell.setCellStyle(cellStyle);
                    dataCell.setCellValue(value.getStartDate());
                  }
                  dataCell = dataOutRow.createCell(cellIndex.incrementAndGet(), CellType.NUMERIC);
                  if (value.getEndDate() != null) {
                    dataCell.setCellStyle(cellStyle);
                    dataCell.setCellValue(value.getEndDate());
                  }
                }
              });
      try (OutputStream fileOut =
          new FileOutputStream("/Users/ironhide/Downloads/Fieraworkflow.xlsx")) {
        wb.write(fileOut);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
