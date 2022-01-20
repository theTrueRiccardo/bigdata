import { coordinata } from "./coordinata";

export interface schedaTaxi {

  rilevazioniTotali : Number,
  rilevazioniGiorno : Number,
  rilevazioniNotte : Number,
  msgConfrontoGiornoNotte : String,
  numeroRilevazioniPerFasciaOraria : Array<[ String, Number]>,
  tempoAttivo : Number,
  tempoFermo : Number,
  percorso : Array< [ String, Array<coordinata>] >

}
