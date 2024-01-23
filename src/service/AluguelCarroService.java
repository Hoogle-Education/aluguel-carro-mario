package service;

import domain.AluguelCarro;
import domain.Recibo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

public class AluguelCarroService {

    private double precoPorHora;
    private double precoPorDia;

    private BrasilTaxaService brasilTaxaService = new BrasilTaxaService();

    public AluguelCarroService() {
    }

    public AluguelCarroService(double precoPorHora, double precoPorDia, BrasilTaxaService brasilTaxaService) {
        this.precoPorHora = precoPorHora;
        this.precoPorDia = precoPorDia;
        this.brasilTaxaService = brasilTaxaService;
    }

    public void processarRecibo(AluguelCarro aluguelCarro){
        // ate 12 horas => cobrava por hora
        // 12h e 1min => 1 dia
        // mais que 12h => cobrava por dia

         Duration duracaoAluguel = Duration.between(aluguelCarro.getInicio(), aluguelCarro.getFim());

         int quantidadeHoras = (int) Math.ceil(duracaoAluguel.toMinutes()/ 60.0);
         int quantidadeDias = (int) Math.ceil(duracaoAluguel.toHours() / 24.0);

//        if(quantidadeHoras <= 12) {
//           pagamentoBase = precoPorHora * quantidadeHoras;
//        } else {
//            pagamentoBase = precoPorDia * quantidadeDias;
//        }

        double pagamentoBase = (quantidadeHoras <= 12)
                ? (precoPorHora * quantidadeHoras)
                : (precoPorDia * quantidadeDias);

        double taxa = brasilTaxaService.taxa(pagamentoBase);

        aluguelCarro.setRecibo(new Recibo(pagamentoBase, taxa));
    }



}
