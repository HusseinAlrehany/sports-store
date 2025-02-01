import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../../../Material.Module';

@Component({
  selector: 'app-image-model',
  standalone: true,
  imports: [MaterialModule],
  templateUrl: './image-model.component.html',
  styleUrl: './image-model.component.scss'
})
export class ImageModelComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public imageUrl: string,
  //refrenece to the dialog to be closed
  private dialogRef: MatDialogRef<ImageModelComponent>

) {}

  closeModal() {
    this.dialogRef.close();
  }

}
