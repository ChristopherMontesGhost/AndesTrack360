package pe.andeslink.andestrack.report;

import pe.andeslink.andestrack.shipment.ShipmentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** Exports operational shipment data as a safe, typed XLSX workbook. */
@Service
public class ShipmentReportService {
    private final ShipmentService shipments;
    public ShipmentReportService(ShipmentService shipments){this.shipments=shipments;}
    public byte[] export()throws IOException{
        try(Workbook workbook=new XSSFWorkbook();ByteArrayOutputStream output=new ByteArrayOutputStream()){
            Sheet sheet=workbook.createSheet("Envíos");sheet.createFreezePane(0,1);sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0,0,0,8));
            String[] headers={"Código","Cliente","Conductor","Camión","Carga","Origen","Destino","Distancia km","ETA"};Row header=sheet.createRow(0);CellStyle style=workbook.createCellStyle();Font font=workbook.createFont();font.setBold(true);style.setFont(font);
            for(int i=0;i<headers.length;i++){Cell cell=header.createCell(i,CellType.STRING);cell.setCellValue(headers[i]);cell.setCellStyle(style);}
            int rowIndex=1;for(var shipment:shipments.all()){Row row=sheet.createRow(rowIndex++);String[] values={shipment.trackingCode(),shipment.clientName(),shipment.driverName(),shipment.truckCode(),shipment.cargo(),shipment.origin(),shipment.destination()};for(int i=0;i<values.length;i++)row.createCell(i,CellType.STRING).setCellValue(values[i]==null?"":values[i]);row.createCell(7,CellType.NUMERIC).setCellValue(shipment.distanceKm());row.createCell(8,CellType.STRING).setCellValue(shipment.eta().toString());}
            for(int i=0;i<headers.length;i++)sheet.autoSizeColumn(i);workbook.write(output);return output.toByteArray();
        }
    }
}