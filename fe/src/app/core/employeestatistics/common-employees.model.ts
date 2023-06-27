export interface CommonEmployeesModel {
  employee1Id: number;
  employee2Id: number;
  elapsedDays: number;
  commonProjects: CommonProjectModel[];
}

export interface CommonProjectModel {
  projectId: number;
  elapsedDays: number;
}
